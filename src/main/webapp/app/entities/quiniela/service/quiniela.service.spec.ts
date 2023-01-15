import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuiniela, Quiniela } from '../quiniela.model';

import { QuinielaService } from './quiniela.service';

describe('Quiniela Service', () => {
  let service: QuinielaService;
  let httpMock: HttpTestingController;
  let elemDefault: IQuiniela;
  let expectedResult: IQuiniela | IQuiniela[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuinielaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      costoParticipacion: 0,
      primerPremio: 0,
      segundoPremio: 0,
      tercerPremio: 0,
      estado: 'AAAAAAA',
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

    it('should create a Quiniela', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Quiniela()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Quiniela', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          costoParticipacion: 1,
          primerPremio: 1,
          segundoPremio: 1,
          tercerPremio: 1,
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Quiniela', () => {
      const patchObject = Object.assign(
        {
          primerPremio: 1,
          segundoPremio: 1,
        },
        new Quiniela()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Quiniela', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          costoParticipacion: 1,
          primerPremio: 1,
          segundoPremio: 1,
          tercerPremio: 1,
          estado: 'BBBBBB',
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

    it('should delete a Quiniela', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addQuinielaToCollectionIfMissing', () => {
      it('should add a Quiniela to an empty array', () => {
        const quiniela: IQuiniela = { id: 123 };
        expectedResult = service.addQuinielaToCollectionIfMissing([], quiniela);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quiniela);
      });

      it('should not add a Quiniela to an array that contains it', () => {
        const quiniela: IQuiniela = { id: 123 };
        const quinielaCollection: IQuiniela[] = [
          {
            ...quiniela,
          },
          { id: 456 },
        ];
        expectedResult = service.addQuinielaToCollectionIfMissing(quinielaCollection, quiniela);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Quiniela to an array that doesn't contain it", () => {
        const quiniela: IQuiniela = { id: 123 };
        const quinielaCollection: IQuiniela[] = [{ id: 456 }];
        expectedResult = service.addQuinielaToCollectionIfMissing(quinielaCollection, quiniela);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quiniela);
      });

      it('should add only unique Quiniela to an array', () => {
        const quinielaArray: IQuiniela[] = [{ id: 123 }, { id: 456 }, { id: 32217 }];
        const quinielaCollection: IQuiniela[] = [{ id: 123 }];
        expectedResult = service.addQuinielaToCollectionIfMissing(quinielaCollection, ...quinielaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const quiniela: IQuiniela = { id: 123 };
        const quiniela2: IQuiniela = { id: 456 };
        expectedResult = service.addQuinielaToCollectionIfMissing([], quiniela, quiniela2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(quiniela);
        expect(expectedResult).toContain(quiniela2);
      });

      it('should accept null and undefined values', () => {
        const quiniela: IQuiniela = { id: 123 };
        expectedResult = service.addQuinielaToCollectionIfMissing([], null, quiniela, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(quiniela);
      });

      it('should return initial array if no Quiniela is added', () => {
        const quinielaCollection: IQuiniela[] = [{ id: 123 }];
        expectedResult = service.addQuinielaToCollectionIfMissing(quinielaCollection, undefined, null);
        expect(expectedResult).toEqual(quinielaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
