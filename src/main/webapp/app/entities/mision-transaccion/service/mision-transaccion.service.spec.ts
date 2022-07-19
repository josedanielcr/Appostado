import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMisionTransaccion, MisionTransaccion } from '../mision-transaccion.model';

import { MisionTransaccionService } from './mision-transaccion.service';

describe('MisionTransaccion Service', () => {
  let service: MisionTransaccionService;
  let httpMock: HttpTestingController;
  let elemDefault: IMisionTransaccion;
  let expectedResult: IMisionTransaccion | IMisionTransaccion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MisionTransaccionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a MisionTransaccion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MisionTransaccion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MisionTransaccion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MisionTransaccion', () => {
      const patchObject = Object.assign({}, new MisionTransaccion());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MisionTransaccion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a MisionTransaccion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMisionTransaccionToCollectionIfMissing', () => {
      it('should add a MisionTransaccion to an empty array', () => {
        const misionTransaccion: IMisionTransaccion = { id: 123 };
        expectedResult = service.addMisionTransaccionToCollectionIfMissing([], misionTransaccion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(misionTransaccion);
      });

      it('should not add a MisionTransaccion to an array that contains it', () => {
        const misionTransaccion: IMisionTransaccion = { id: 123 };
        const misionTransaccionCollection: IMisionTransaccion[] = [
          {
            ...misionTransaccion,
          },
          { id: 456 },
        ];
        expectedResult = service.addMisionTransaccionToCollectionIfMissing(misionTransaccionCollection, misionTransaccion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MisionTransaccion to an array that doesn't contain it", () => {
        const misionTransaccion: IMisionTransaccion = { id: 123 };
        const misionTransaccionCollection: IMisionTransaccion[] = [{ id: 456 }];
        expectedResult = service.addMisionTransaccionToCollectionIfMissing(misionTransaccionCollection, misionTransaccion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(misionTransaccion);
      });

      it('should add only unique MisionTransaccion to an array', () => {
        const misionTransaccionArray: IMisionTransaccion[] = [{ id: 123 }, { id: 456 }, { id: 16545 }];
        const misionTransaccionCollection: IMisionTransaccion[] = [{ id: 123 }];
        expectedResult = service.addMisionTransaccionToCollectionIfMissing(misionTransaccionCollection, ...misionTransaccionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const misionTransaccion: IMisionTransaccion = { id: 123 };
        const misionTransaccion2: IMisionTransaccion = { id: 456 };
        expectedResult = service.addMisionTransaccionToCollectionIfMissing([], misionTransaccion, misionTransaccion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(misionTransaccion);
        expect(expectedResult).toContain(misionTransaccion2);
      });

      it('should accept null and undefined values', () => {
        const misionTransaccion: IMisionTransaccion = { id: 123 };
        expectedResult = service.addMisionTransaccionToCollectionIfMissing([], null, misionTransaccion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(misionTransaccion);
      });

      it('should return initial array if no MisionTransaccion is added', () => {
        const misionTransaccionCollection: IMisionTransaccion[] = [{ id: 123 }];
        expectedResult = service.addMisionTransaccionToCollectionIfMissing(misionTransaccionCollection, undefined, null);
        expect(expectedResult).toEqual(misionTransaccionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
