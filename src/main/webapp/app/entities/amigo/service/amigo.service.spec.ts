import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAmigo, Amigo } from '../amigo.model';

import { AmigoService } from './amigo.service';

describe('Amigo Service', () => {
  let service: AmigoService;
  let httpMock: HttpTestingController;
  let elemDefault: IAmigo;
  let expectedResult: IAmigo | IAmigo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AmigoService);
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

    it('should create a Amigo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Amigo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Amigo', () => {
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

    it('should partial update a Amigo', () => {
      const patchObject = Object.assign({}, new Amigo());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Amigo', () => {
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

    it('should delete a Amigo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAmigoToCollectionIfMissing', () => {
      it('should add a Amigo to an empty array', () => {
        const amigo: IAmigo = { id: 123 };
        expectedResult = service.addAmigoToCollectionIfMissing([], amigo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amigo);
      });

      it('should not add a Amigo to an array that contains it', () => {
        const amigo: IAmigo = { id: 123 };
        const amigoCollection: IAmigo[] = [
          {
            ...amigo,
          },
          { id: 456 },
        ];
        expectedResult = service.addAmigoToCollectionIfMissing(amigoCollection, amigo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Amigo to an array that doesn't contain it", () => {
        const amigo: IAmigo = { id: 123 };
        const amigoCollection: IAmigo[] = [{ id: 456 }];
        expectedResult = service.addAmigoToCollectionIfMissing(amigoCollection, amigo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amigo);
      });

      it('should add only unique Amigo to an array', () => {
        const amigoArray: IAmigo[] = [{ id: 123 }, { id: 456 }, { id: 19889 }];
        const amigoCollection: IAmigo[] = [{ id: 123 }];
        expectedResult = service.addAmigoToCollectionIfMissing(amigoCollection, ...amigoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const amigo: IAmigo = { id: 123 };
        const amigo2: IAmigo = { id: 456 };
        expectedResult = service.addAmigoToCollectionIfMissing([], amigo, amigo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amigo);
        expect(expectedResult).toContain(amigo2);
      });

      it('should accept null and undefined values', () => {
        const amigo: IAmigo = { id: 123 };
        expectedResult = service.addAmigoToCollectionIfMissing([], null, amigo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amigo);
      });

      it('should return initial array if no Amigo is added', () => {
        const amigoCollection: IAmigo[] = [{ id: 123 }];
        expectedResult = service.addAmigoToCollectionIfMissing(amigoCollection, undefined, null);
        expect(expectedResult).toEqual(amigoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
