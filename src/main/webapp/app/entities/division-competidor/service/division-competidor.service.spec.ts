import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDivisionCompetidor, DivisionCompetidor } from '../division-competidor.model';

import { DivisionCompetidorService } from './division-competidor.service';

describe('DivisionCompetidor Service', () => {
  let service: DivisionCompetidorService;
  let httpMock: HttpTestingController;
  let elemDefault: IDivisionCompetidor;
  let expectedResult: IDivisionCompetidor | IDivisionCompetidor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DivisionCompetidorService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idDivision: 0,
      idCompetidor: 0,
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

    it('should create a DivisionCompetidor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DivisionCompetidor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DivisionCompetidor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idDivision: 1,
          idCompetidor: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DivisionCompetidor', () => {
      const patchObject = Object.assign(
        {
          idDivision: 1,
          idCompetidor: 1,
        },
        new DivisionCompetidor()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DivisionCompetidor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idDivision: 1,
          idCompetidor: 1,
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

    it('should delete a DivisionCompetidor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDivisionCompetidorToCollectionIfMissing', () => {
      it('should add a DivisionCompetidor to an empty array', () => {
        const divisionCompetidor: IDivisionCompetidor = { id: 123 };
        expectedResult = service.addDivisionCompetidorToCollectionIfMissing([], divisionCompetidor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(divisionCompetidor);
      });

      it('should not add a DivisionCompetidor to an array that contains it', () => {
        const divisionCompetidor: IDivisionCompetidor = { id: 123 };
        const divisionCompetidorCollection: IDivisionCompetidor[] = [
          {
            ...divisionCompetidor,
          },
          { id: 456 },
        ];
        expectedResult = service.addDivisionCompetidorToCollectionIfMissing(divisionCompetidorCollection, divisionCompetidor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DivisionCompetidor to an array that doesn't contain it", () => {
        const divisionCompetidor: IDivisionCompetidor = { id: 123 };
        const divisionCompetidorCollection: IDivisionCompetidor[] = [{ id: 456 }];
        expectedResult = service.addDivisionCompetidorToCollectionIfMissing(divisionCompetidorCollection, divisionCompetidor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(divisionCompetidor);
      });

      it('should add only unique DivisionCompetidor to an array', () => {
        const divisionCompetidorArray: IDivisionCompetidor[] = [{ id: 123 }, { id: 456 }, { id: 44535 }];
        const divisionCompetidorCollection: IDivisionCompetidor[] = [{ id: 123 }];
        expectedResult = service.addDivisionCompetidorToCollectionIfMissing(divisionCompetidorCollection, ...divisionCompetidorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const divisionCompetidor: IDivisionCompetidor = { id: 123 };
        const divisionCompetidor2: IDivisionCompetidor = { id: 456 };
        expectedResult = service.addDivisionCompetidorToCollectionIfMissing([], divisionCompetidor, divisionCompetidor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(divisionCompetidor);
        expect(expectedResult).toContain(divisionCompetidor2);
      });

      it('should accept null and undefined values', () => {
        const divisionCompetidor: IDivisionCompetidor = { id: 123 };
        expectedResult = service.addDivisionCompetidorToCollectionIfMissing([], null, divisionCompetidor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(divisionCompetidor);
      });

      it('should return initial array if no DivisionCompetidor is added', () => {
        const divisionCompetidorCollection: IDivisionCompetidor[] = [{ id: 123 }];
        expectedResult = service.addDivisionCompetidorToCollectionIfMissing(divisionCompetidorCollection, undefined, null);
        expect(expectedResult).toEqual(divisionCompetidorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
