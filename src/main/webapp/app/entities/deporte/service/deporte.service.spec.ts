import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeporte, Deporte } from '../deporte.model';

import { DeporteService } from './deporte.service';

describe('Deporte Service', () => {
  let service: DeporteService;
  let httpMock: HttpTestingController;
  let elemDefault: IDeporte;
  let expectedResult: IDeporte | IDeporte[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeporteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Deporte', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Deporte()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Deporte', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Deporte', () => {
      const patchObject = Object.assign({}, new Deporte());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Deporte', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Deporte', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDeporteToCollectionIfMissing', () => {
      it('should add a Deporte to an empty array', () => {
        const deporte: IDeporte = { id: 123 };
        expectedResult = service.addDeporteToCollectionIfMissing([], deporte);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deporte);
      });

      it('should not add a Deporte to an array that contains it', () => {
        const deporte: IDeporte = { id: 123 };
        const deporteCollection: IDeporte[] = [
          {
            ...deporte,
          },
          { id: 456 },
        ];
        expectedResult = service.addDeporteToCollectionIfMissing(deporteCollection, deporte);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Deporte to an array that doesn't contain it", () => {
        const deporte: IDeporte = { id: 123 };
        const deporteCollection: IDeporte[] = [{ id: 456 }];
        expectedResult = service.addDeporteToCollectionIfMissing(deporteCollection, deporte);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deporte);
      });

      it('should add only unique Deporte to an array', () => {
        const deporteArray: IDeporte[] = [{ id: 123 }, { id: 456 }, { id: 12631 }];
        const deporteCollection: IDeporte[] = [{ id: 123 }];
        expectedResult = service.addDeporteToCollectionIfMissing(deporteCollection, ...deporteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deporte: IDeporte = { id: 123 };
        const deporte2: IDeporte = { id: 456 };
        expectedResult = service.addDeporteToCollectionIfMissing([], deporte, deporte2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deporte);
        expect(expectedResult).toContain(deporte2);
      });

      it('should accept null and undefined values', () => {
        const deporte: IDeporte = { id: 123 };
        expectedResult = service.addDeporteToCollectionIfMissing([], null, deporte, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deporte);
      });

      it('should return initial array if no Deporte is added', () => {
        const deporteCollection: IDeporte[] = [{ id: 123 }];
        expectedResult = service.addDeporteToCollectionIfMissing(deporteCollection, undefined, null);
        expect(expectedResult).toEqual(deporteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
