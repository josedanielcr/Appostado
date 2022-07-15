import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IApuestaTransaccion, ApuestaTransaccion } from '../apuesta-transaccion.model';
import { ApuestaTransaccionService } from '../service/apuesta-transaccion.service';

import { ApuestaTransaccionRoutingResolveService } from './apuesta-transaccion-routing-resolve.service';

describe('ApuestaTransaccion routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ApuestaTransaccionRoutingResolveService;
  let service: ApuestaTransaccionService;
  let resultApuestaTransaccion: IApuestaTransaccion | undefined;

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
    routingResolveService = TestBed.inject(ApuestaTransaccionRoutingResolveService);
    service = TestBed.inject(ApuestaTransaccionService);
    resultApuestaTransaccion = undefined;
  });

  describe('resolve', () => {
    it('should return IApuestaTransaccion returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultApuestaTransaccion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultApuestaTransaccion).toEqual({ id: 123 });
    });

    it('should return new IApuestaTransaccion if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultApuestaTransaccion = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultApuestaTransaccion).toEqual(new ApuestaTransaccion());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ApuestaTransaccion })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultApuestaTransaccion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultApuestaTransaccion).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
