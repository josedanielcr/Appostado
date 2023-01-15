import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMisionTransaccion, MisionTransaccion } from '../mision-transaccion.model';
import { MisionTransaccionService } from '../service/mision-transaccion.service';

import { MisionTransaccionRoutingResolveService } from './mision-transaccion-routing-resolve.service';

describe('MisionTransaccion routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MisionTransaccionRoutingResolveService;
  let service: MisionTransaccionService;
  let resultMisionTransaccion: IMisionTransaccion | undefined;

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
    routingResolveService = TestBed.inject(MisionTransaccionRoutingResolveService);
    service = TestBed.inject(MisionTransaccionService);
    resultMisionTransaccion = undefined;
  });

  describe('resolve', () => {
    it('should return IMisionTransaccion returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMisionTransaccion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMisionTransaccion).toEqual({ id: 123 });
    });

    it('should return new IMisionTransaccion if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMisionTransaccion = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMisionTransaccion).toEqual(new MisionTransaccion());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MisionTransaccion })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMisionTransaccion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMisionTransaccion).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
