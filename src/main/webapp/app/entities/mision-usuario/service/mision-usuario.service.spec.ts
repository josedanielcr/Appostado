import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMisionUsuario, MisionUsuario } from '../mision-usuario.model';

import { MisionUsuarioService } from './mision-usuario.service';

describe('MisionUsuario Service', () => {
  let service: MisionUsuarioService;
  let httpMock: HttpTestingController;
  let elemDefault: IMisionUsuario;
  let expectedResult: IMisionUsuario | IMisionUsuario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MisionUsuarioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idMision: 0,
      idUsuario: 0,
      completado: false,
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

    it('should create a MisionUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MisionUsuario()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MisionUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idMision: 1,
          idUsuario: 1,
          completado: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MisionUsuario', () => {
      const patchObject = Object.assign(
        {
          idMision: 1,
          completado: true,
        },
        new MisionUsuario()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MisionUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idMision: 1,
          idUsuario: 1,
          completado: true,
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

    it('should delete a MisionUsuario', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMisionUsuarioToCollectionIfMissing', () => {
      it('should add a MisionUsuario to an empty array', () => {
        const misionUsuario: IMisionUsuario = { id: 123 };
        expectedResult = service.addMisionUsuarioToCollectionIfMissing([], misionUsuario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(misionUsuario);
      });

      it('should not add a MisionUsuario to an array that contains it', () => {
        const misionUsuario: IMisionUsuario = { id: 123 };
        const misionUsuarioCollection: IMisionUsuario[] = [
          {
            ...misionUsuario,
          },
          { id: 456 },
        ];
        expectedResult = service.addMisionUsuarioToCollectionIfMissing(misionUsuarioCollection, misionUsuario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MisionUsuario to an array that doesn't contain it", () => {
        const misionUsuario: IMisionUsuario = { id: 123 };
        const misionUsuarioCollection: IMisionUsuario[] = [{ id: 456 }];
        expectedResult = service.addMisionUsuarioToCollectionIfMissing(misionUsuarioCollection, misionUsuario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(misionUsuario);
      });

      it('should add only unique MisionUsuario to an array', () => {
        const misionUsuarioArray: IMisionUsuario[] = [{ id: 123 }, { id: 456 }, { id: 14438 }];
        const misionUsuarioCollection: IMisionUsuario[] = [{ id: 123 }];
        expectedResult = service.addMisionUsuarioToCollectionIfMissing(misionUsuarioCollection, ...misionUsuarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const misionUsuario: IMisionUsuario = { id: 123 };
        const misionUsuario2: IMisionUsuario = { id: 456 };
        expectedResult = service.addMisionUsuarioToCollectionIfMissing([], misionUsuario, misionUsuario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(misionUsuario);
        expect(expectedResult).toContain(misionUsuario2);
      });

      it('should accept null and undefined values', () => {
        const misionUsuario: IMisionUsuario = { id: 123 };
        expectedResult = service.addMisionUsuarioToCollectionIfMissing([], null, misionUsuario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(misionUsuario);
      });

      it('should return initial array if no MisionUsuario is added', () => {
        const misionUsuarioCollection: IMisionUsuario[] = [{ id: 123 }];
        expectedResult = service.addMisionUsuarioToCollectionIfMissing(misionUsuarioCollection, undefined, null);
        expect(expectedResult).toEqual(misionUsuarioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
