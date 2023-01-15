import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IProductoUsuario, ProductoUsuario } from '../producto-usuario.model';
import { ProductoUsuarioService } from '../service/producto-usuario.service';

import { ProductoUsuarioRoutingResolveService } from './producto-usuario-routing-resolve.service';

describe('ProductoUsuario routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ProductoUsuarioRoutingResolveService;
  let service: ProductoUsuarioService;
  let resultProductoUsuario: IProductoUsuario | undefined;

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
    routingResolveService = TestBed.inject(ProductoUsuarioRoutingResolveService);
    service = TestBed.inject(ProductoUsuarioService);
    resultProductoUsuario = undefined;
  });

  describe('resolve', () => {
    it('should return IProductoUsuario returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductoUsuario = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductoUsuario).toEqual({ id: 123 });
    });

    it('should return new IProductoUsuario if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductoUsuario = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProductoUsuario).toEqual(new ProductoUsuario());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProductoUsuario })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductoUsuario = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductoUsuario).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
