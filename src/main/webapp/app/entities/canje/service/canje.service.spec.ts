import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICanje, Canje } from '../canje.model';

import { CanjeService } from './canje.service';

describe('Canje Service', () => {
  let service: CanjeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICanje;
  let expectedResult: ICanje | ICanje[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CanjeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      estado: 'AAAAAAA',
      detalle: 'AAAAAAA',
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

    it('should create a Canje', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Canje()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Canje', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          estado: 'BBBBBB',
          detalle: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Canje', () => {
      const patchObject = Object.assign(
        {
          estado: 'BBBBBB',
          detalle: 'BBBBBB',
        },
        new Canje()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Canje', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          estado: 'BBBBBB',
          detalle: 'BBBBBB',
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

    it('should delete a Canje', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCanjeToCollectionIfMissing', () => {
      it('should add a Canje to an empty array', () => {
        const canje: ICanje = { id: 123 };
        expectedResult = service.addCanjeToCollectionIfMissing([], canje);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(canje);
      });

      it('should not add a Canje to an array that contains it', () => {
        const canje: ICanje = { id: 123 };
        const canjeCollection: ICanje[] = [
          {
            ...canje,
          },
          { id: 456 },
        ];
        expectedResult = service.addCanjeToCollectionIfMissing(canjeCollection, canje);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Canje to an array that doesn't contain it", () => {
        const canje: ICanje = { id: 123 };
        const canjeCollection: ICanje[] = [{ id: 456 }];
        expectedResult = service.addCanjeToCollectionIfMissing(canjeCollection, canje);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(canje);
      });

      it('should add only unique Canje to an array', () => {
        const canjeArray: ICanje[] = [{ id: 123 }, { id: 456 }, { id: 39123 }];
        const canjeCollection: ICanje[] = [{ id: 123 }];
        expectedResult = service.addCanjeToCollectionIfMissing(canjeCollection, ...canjeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const canje: ICanje = { id: 123 };
        const canje2: ICanje = { id: 456 };
        expectedResult = service.addCanjeToCollectionIfMissing([], canje, canje2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(canje);
        expect(expectedResult).toContain(canje2);
      });

      it('should accept null and undefined values', () => {
        const canje: ICanje = { id: 123 };
        expectedResult = service.addCanjeToCollectionIfMissing([], null, canje, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(canje);
      });

      it('should return initial array if no Canje is added', () => {
        const canjeCollection: ICanje[] = [{ id: 123 }];
        expectedResult = service.addCanjeToCollectionIfMissing(canjeCollection, undefined, null);
        expect(expectedResult).toEqual(canjeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
