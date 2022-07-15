import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompetidor, Competidor } from '../competidor.model';

import { CompetidorService } from './competidor.service';

describe('Competidor Service', () => {
  let service: CompetidorService;
  let httpMock: HttpTestingController;
  let elemDefault: ICompetidor;
  let expectedResult: ICompetidor | ICompetidor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompetidorService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      foto: 'AAAAAAA',
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

    it('should create a Competidor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Competidor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Competidor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          foto: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Competidor', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
        },
        new Competidor()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Competidor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          foto: 'BBBBBB',
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

    it('should delete a Competidor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCompetidorToCollectionIfMissing', () => {
      it('should add a Competidor to an empty array', () => {
        const competidor: ICompetidor = { id: 123 };
        expectedResult = service.addCompetidorToCollectionIfMissing([], competidor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(competidor);
      });

      it('should not add a Competidor to an array that contains it', () => {
        const competidor: ICompetidor = { id: 123 };
        const competidorCollection: ICompetidor[] = [
          {
            ...competidor,
          },
          { id: 456 },
        ];
        expectedResult = service.addCompetidorToCollectionIfMissing(competidorCollection, competidor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Competidor to an array that doesn't contain it", () => {
        const competidor: ICompetidor = { id: 123 };
        const competidorCollection: ICompetidor[] = [{ id: 456 }];
        expectedResult = service.addCompetidorToCollectionIfMissing(competidorCollection, competidor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(competidor);
      });

      it('should add only unique Competidor to an array', () => {
        const competidorArray: ICompetidor[] = [{ id: 123 }, { id: 456 }, { id: 33075 }];
        const competidorCollection: ICompetidor[] = [{ id: 123 }];
        expectedResult = service.addCompetidorToCollectionIfMissing(competidorCollection, ...competidorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const competidor: ICompetidor = { id: 123 };
        const competidor2: ICompetidor = { id: 456 };
        expectedResult = service.addCompetidorToCollectionIfMissing([], competidor, competidor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(competidor);
        expect(expectedResult).toContain(competidor2);
      });

      it('should accept null and undefined values', () => {
        const competidor: ICompetidor = { id: 123 };
        expectedResult = service.addCompetidorToCollectionIfMissing([], null, competidor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(competidor);
      });

      it('should return initial array if no Competidor is added', () => {
        const competidorCollection: ICompetidor[] = [{ id: 123 }];
        expectedResult = service.addCompetidorToCollectionIfMissing(competidorCollection, undefined, null);
        expect(expectedResult).toEqual(competidorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
