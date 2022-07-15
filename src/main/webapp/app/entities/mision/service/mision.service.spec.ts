import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMision, Mision } from '../mision.model';

import { MisionService } from './mision.service';

describe('Mision Service', () => {
  let service: MisionService;
  let httpMock: HttpTestingController;
  let elemDefault: IMision;
  let expectedResult: IMision | IMision[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MisionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      bonoCreditos: 0,
      dia: 'AAAAAAA',
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

    it('should create a Mision', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Mision()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mision', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          bonoCreditos: 1,
          dia: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mision', () => {
      const patchObject = Object.assign({}, new Mision());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mision', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          bonoCreditos: 1,
          dia: 'BBBBBB',
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

    it('should delete a Mision', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMisionToCollectionIfMissing', () => {
      it('should add a Mision to an empty array', () => {
        const mision: IMision = { id: 123 };
        expectedResult = service.addMisionToCollectionIfMissing([], mision);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mision);
      });

      it('should not add a Mision to an array that contains it', () => {
        const mision: IMision = { id: 123 };
        const misionCollection: IMision[] = [
          {
            ...mision,
          },
          { id: 456 },
        ];
        expectedResult = service.addMisionToCollectionIfMissing(misionCollection, mision);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mision to an array that doesn't contain it", () => {
        const mision: IMision = { id: 123 };
        const misionCollection: IMision[] = [{ id: 456 }];
        expectedResult = service.addMisionToCollectionIfMissing(misionCollection, mision);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mision);
      });

      it('should add only unique Mision to an array', () => {
        const misionArray: IMision[] = [{ id: 123 }, { id: 456 }, { id: 17690 }];
        const misionCollection: IMision[] = [{ id: 123 }];
        expectedResult = service.addMisionToCollectionIfMissing(misionCollection, ...misionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mision: IMision = { id: 123 };
        const mision2: IMision = { id: 456 };
        expectedResult = service.addMisionToCollectionIfMissing([], mision, mision2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mision);
        expect(expectedResult).toContain(mision2);
      });

      it('should accept null and undefined values', () => {
        const mision: IMision = { id: 123 };
        expectedResult = service.addMisionToCollectionIfMissing([], null, mision, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mision);
      });

      it('should return initial array if no Mision is added', () => {
        const misionCollection: IMision[] = [{ id: 123 }];
        expectedResult = service.addMisionToCollectionIfMissing(misionCollection, undefined, null);
        expect(expectedResult).toEqual(misionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
