import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITransaccion, Transaccion } from '../transaccion.model';

import { TransaccionService } from './transaccion.service';

describe('Transaccion Service', () => {
  let service: TransaccionService;
  let httpMock: HttpTestingController;
  let elemDefault: ITransaccion;
  let expectedResult: ITransaccion | ITransaccion[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransaccionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fecha: currentDate,
      tipo: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      monto: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fecha: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Transaccion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fecha: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.create(new Transaccion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Transaccion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fecha: currentDate.format(DATE_FORMAT),
          tipo: 'BBBBBB',
          descripcion: 'BBBBBB',
          monto: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Transaccion', () => {
      const patchObject = Object.assign(
        {
          fecha: currentDate.format(DATE_FORMAT),
          descripcion: 'BBBBBB',
          monto: 1,
        },
        new Transaccion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Transaccion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fecha: currentDate.format(DATE_FORMAT),
          tipo: 'BBBBBB',
          descripcion: 'BBBBBB',
          monto: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Transaccion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTransaccionToCollectionIfMissing', () => {
      it('should add a Transaccion to an empty array', () => {
        const transaccion: ITransaccion = { id: 123 };
        expectedResult = service.addTransaccionToCollectionIfMissing([], transaccion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transaccion);
      });

      it('should not add a Transaccion to an array that contains it', () => {
        const transaccion: ITransaccion = { id: 123 };
        const transaccionCollection: ITransaccion[] = [
          {
            ...transaccion,
          },
          { id: 456 },
        ];
        expectedResult = service.addTransaccionToCollectionIfMissing(transaccionCollection, transaccion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Transaccion to an array that doesn't contain it", () => {
        const transaccion: ITransaccion = { id: 123 };
        const transaccionCollection: ITransaccion[] = [{ id: 456 }];
        expectedResult = service.addTransaccionToCollectionIfMissing(transaccionCollection, transaccion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transaccion);
      });

      it('should add only unique Transaccion to an array', () => {
        const transaccionArray: ITransaccion[] = [{ id: 123 }, { id: 456 }, { id: 2706 }];
        const transaccionCollection: ITransaccion[] = [{ id: 123 }];
        expectedResult = service.addTransaccionToCollectionIfMissing(transaccionCollection, ...transaccionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transaccion: ITransaccion = { id: 123 };
        const transaccion2: ITransaccion = { id: 456 };
        expectedResult = service.addTransaccionToCollectionIfMissing([], transaccion, transaccion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transaccion);
        expect(expectedResult).toContain(transaccion2);
      });

      it('should accept null and undefined values', () => {
        const transaccion: ITransaccion = { id: 123 };
        expectedResult = service.addTransaccionToCollectionIfMissing([], null, transaccion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transaccion);
      });

      it('should return initial array if no Transaccion is added', () => {
        const transaccionCollection: ITransaccion[] = [{ id: 123 }];
        expectedResult = service.addTransaccionToCollectionIfMissing(transaccionCollection, undefined, null);
        expect(expectedResult).toEqual(transaccionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
