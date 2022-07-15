import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILiga, Liga } from '../liga.model';

import { LigaService } from './liga.service';

describe('Liga Service', () => {
  let service: LigaService;
  let httpMock: HttpTestingController;
  let elemDefault: ILiga;
  let expectedResult: ILiga | ILiga[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LigaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
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

    it('should create a Liga', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Liga()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Liga', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
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

    it('should partial update a Liga', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        new Liga()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Liga', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
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

    it('should delete a Liga', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLigaToCollectionIfMissing', () => {
      it('should add a Liga to an empty array', () => {
        const liga: ILiga = { id: 123 };
        expectedResult = service.addLigaToCollectionIfMissing([], liga);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(liga);
      });

      it('should not add a Liga to an array that contains it', () => {
        const liga: ILiga = { id: 123 };
        const ligaCollection: ILiga[] = [
          {
            ...liga,
          },
          { id: 456 },
        ];
        expectedResult = service.addLigaToCollectionIfMissing(ligaCollection, liga);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Liga to an array that doesn't contain it", () => {
        const liga: ILiga = { id: 123 };
        const ligaCollection: ILiga[] = [{ id: 456 }];
        expectedResult = service.addLigaToCollectionIfMissing(ligaCollection, liga);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(liga);
      });

      it('should add only unique Liga to an array', () => {
        const ligaArray: ILiga[] = [{ id: 123 }, { id: 456 }, { id: 79892 }];
        const ligaCollection: ILiga[] = [{ id: 123 }];
        expectedResult = service.addLigaToCollectionIfMissing(ligaCollection, ...ligaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const liga: ILiga = { id: 123 };
        const liga2: ILiga = { id: 456 };
        expectedResult = service.addLigaToCollectionIfMissing([], liga, liga2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(liga);
        expect(expectedResult).toContain(liga2);
      });

      it('should accept null and undefined values', () => {
        const liga: ILiga = { id: 123 };
        expectedResult = service.addLigaToCollectionIfMissing([], null, liga, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(liga);
      });

      it('should return initial array if no Liga is added', () => {
        const ligaCollection: ILiga[] = [{ id: 123 }];
        expectedResult = service.addLigaToCollectionIfMissing(ligaCollection, undefined, null);
        expect(expectedResult).toEqual(ligaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
