import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ILigaUsuario, LigaUsuario } from '../liga-usuario.model';
import { LigaUsuarioService } from '../service/liga-usuario.service';

import { LigaUsuarioRoutingResolveService } from './liga-usuario-routing-resolve.service';

describe('LigaUsuario routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LigaUsuarioRoutingResolveService;
  let service: LigaUsuarioService;
  let resultLigaUsuario: ILigaUsuario | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(LigaUsuarioRoutingResolveService);
    service = TestBed.inject(LigaUsuarioService);
    resultLigaUsuario = undefined;
  });

  describe('resolve', () => {
    it('should return ILigaUsuario returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLigaUsuario = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLigaUsuario).toEqual({ id: 123 });
    });

    it('should return new ILigaUsuario if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLigaUsuario = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLigaUsuario).toEqual(new LigaUsuario());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LigaUsuario })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLigaUsuario = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLigaUsuario).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
