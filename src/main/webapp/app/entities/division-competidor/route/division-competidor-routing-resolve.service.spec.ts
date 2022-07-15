import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDivisionCompetidor, DivisionCompetidor } from '../division-competidor.model';
import { DivisionCompetidorService } from '../service/division-competidor.service';

import { DivisionCompetidorRoutingResolveService } from './division-competidor-routing-resolve.service';

describe('DivisionCompetidor routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DivisionCompetidorRoutingResolveService;
  let service: DivisionCompetidorService;
  let resultDivisionCompetidor: IDivisionCompetidor | undefined;

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
    routingResolveService = TestBed.inject(DivisionCompetidorRoutingResolveService);
    service = TestBed.inject(DivisionCompetidorService);
    resultDivisionCompetidor = undefined;
  });

  describe('resolve', () => {
    it('should return IDivisionCompetidor returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDivisionCompetidor = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDivisionCompetidor).toEqual({ id: 123 });
    });

    it('should return new IDivisionCompetidor if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDivisionCompetidor = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDivisionCompetidor).toEqual(new DivisionCompetidor());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DivisionCompetidor })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDivisionCompetidor = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDivisionCompetidor).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
