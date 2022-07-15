import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApuestaTransaccion, ApuestaTransaccion } from '../apuesta-transaccion.model';

import { ApuestaTransaccionService } from './apuesta-transaccion.service';

describe('ApuestaTransaccion Service', () => {
  let service: ApuestaTransaccionService;
  let httpMock: HttpTestingController;
  let elemDefault: IApuestaTransaccion;
  let expectedResult: IApuestaTransaccion | IApuestaTransaccion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApuestaTransaccionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idApuesta: 0,
      idTransaccion: 0,
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

    it('should create a ApuestaTransaccion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ApuestaTransaccion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApuestaTransaccion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idApuesta: 1,
          idTransaccion: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApuestaTransaccion', () => {
      const patchObject = Object.assign(
        {
          idTransaccion: 1,
        },
        new ApuestaTransaccion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApuestaTransaccion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idApuesta: 1,
          idTransaccion: 1,
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

    it('should delete a ApuestaTransaccion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addApuestaTransaccionToCollectionIfMissing', () => {
      it('should add a ApuestaTransaccion to an empty array', () => {
        const apuestaTransaccion: IApuestaTransaccion = { id: 123 };
        expectedResult = service.addApuestaTransaccionToCollectionIfMissing([], apuestaTransaccion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apuestaTransaccion);
      });

      it('should not add a ApuestaTransaccion to an array that contains it', () => {
        const apuestaTransaccion: IApuestaTransaccion = { id: 123 };
        const apuestaTransaccionCollection: IApuestaTransaccion[] = [
          {
            ...apuestaTransaccion,
          },
          { id: 456 },
        ];
        expectedResult = service.addApuestaTransaccionToCollectionIfMissing(apuestaTransaccionCollection, apuestaTransaccion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApuestaTransaccion to an array that doesn't contain it", () => {
        const apuestaTransaccion: IApuestaTransaccion = { id: 123 };
        const apuestaTransaccionCollection: IApuestaTransaccion[] = [{ id: 456 }];
        expectedResult = service.addApuestaTransaccionToCollectionIfMissing(apuestaTransaccionCollection, apuestaTransaccion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apuestaTransaccion);
      });

      it('should add only unique ApuestaTransaccion to an array', () => {
        const apuestaTransaccionArray: IApuestaTransaccion[] = [{ id: 123 }, { id: 456 }, { id: 50241 }];
        const apuestaTransaccionCollection: IApuestaTransaccion[] = [{ id: 123 }];
        expectedResult = service.addApuestaTransaccionToCollectionIfMissing(apuestaTransaccionCollection, ...apuestaTransaccionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const apuestaTransaccion: IApuestaTransaccion = { id: 123 };
        const apuestaTransaccion2: IApuestaTransaccion = { id: 456 };
        expectedResult = service.addApuestaTransaccionToCollectionIfMissing([], apuestaTransaccion, apuestaTransaccion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apuestaTransaccion);
        expect(expectedResult).toContain(apuestaTransaccion2);
      });

      it('should accept null and undefined values', () => {
        const apuestaTransaccion: IApuestaTransaccion = { id: 123 };
        expectedResult = service.addApuestaTransaccionToCollectionIfMissing([], null, apuestaTransaccion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apuestaTransaccion);
      });

      it('should return initial array if no ApuestaTransaccion is added', () => {
        const apuestaTransaccionCollection: IApuestaTransaccion[] = [{ id: 123 }];
        expectedResult = service.addApuestaTransaccionToCollectionIfMissing(apuestaTransaccionCollection, undefined, null);
        expect(expectedResult).toEqual(apuestaTransaccionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
