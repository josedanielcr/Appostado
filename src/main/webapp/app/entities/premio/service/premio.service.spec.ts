import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPremio, Premio } from '../premio.model';

import { PremioService } from './premio.service';

describe('Premio Service', () => {
  let service: PremioService;
  let httpMock: HttpTestingController;
  let elemDefault: IPremio;
  let expectedResult: IPremio | IPremio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PremioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      foto: 'AAAAAAA',
      costo: 0,
      estado: 'AAAAAAA',
      stock: 0,
      numCanjes: 0,
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

    it('should create a Premio', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Premio()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Premio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          foto: 'BBBBBB',
          costo: 1,
          estado: 'BBBBBB',
          stock: 1,
          numCanjes: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Premio', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          foto: 'BBBBBB',
          costo: 1,
          numCanjes: 1,
        },
        new Premio()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Premio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          foto: 'BBBBBB',
          costo: 1,
          estado: 'BBBBBB',
          stock: 1,
          numCanjes: 1,
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

    it('should delete a Premio', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPremioToCollectionIfMissing', () => {
      it('should add a Premio to an empty array', () => {
        const premio: IPremio = { id: 123 };
        expectedResult = service.addPremioToCollectionIfMissing([], premio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(premio);
      });

      it('should not add a Premio to an array that contains it', () => {
        const premio: IPremio = { id: 123 };
        const premioCollection: IPremio[] = [
          {
            ...premio,
          },
          { id: 456 },
        ];
        expectedResult = service.addPremioToCollectionIfMissing(premioCollection, premio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Premio to an array that doesn't contain it", () => {
        const premio: IPremio = { id: 123 };
        const premioCollection: IPremio[] = [{ id: 456 }];
        expectedResult = service.addPremioToCollectionIfMissing(premioCollection, premio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(premio);
      });

      it('should add only unique Premio to an array', () => {
        const premioArray: IPremio[] = [{ id: 123 }, { id: 456 }, { id: 13243 }];
        const premioCollection: IPremio[] = [{ id: 123 }];
        expectedResult = service.addPremioToCollectionIfMissing(premioCollection, ...premioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const premio: IPremio = { id: 123 };
        const premio2: IPremio = { id: 456 };
        expectedResult = service.addPremioToCollectionIfMissing([], premio, premio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(premio);
        expect(expectedResult).toContain(premio2);
      });

      it('should accept null and undefined values', () => {
        const premio: IPremio = { id: 123 };
        expectedResult = service.addPremioToCollectionIfMissing([], null, premio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(premio);
      });

      it('should return initial array if no Premio is added', () => {
        const premioCollection: IPremio[] = [{ id: 123 }];
        expectedResult = service.addPremioToCollectionIfMissing(premioCollection, undefined, null);
        expect(expectedResult).toEqual(premioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
