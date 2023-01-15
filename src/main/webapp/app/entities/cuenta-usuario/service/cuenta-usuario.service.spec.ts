import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICuentaUsuario, CuentaUsuario } from '../cuenta-usuario.model';

import { CuentaUsuarioService } from './cuenta-usuario.service';

describe('CuentaUsuario Service', () => {
  let service: CuentaUsuarioService;
  let httpMock: HttpTestingController;
  let elemDefault: ICuentaUsuario;
  let expectedResult: ICuentaUsuario | ICuentaUsuario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CuentaUsuarioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      balance: 0,
      numCanjes: 0,
      apuestasTotales: 0,
      apuestasGanadas: 0,
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

    it('should create a CuentaUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CuentaUsuario()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CuentaUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          balance: 1,
          numCanjes: 1,
          apuestasTotales: 1,
          apuestasGanadas: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CuentaUsuario', () => {
      const patchObject = Object.assign(
        {
          balance: 1,
        },
        new CuentaUsuario()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CuentaUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          balance: 1,
          numCanjes: 1,
          apuestasTotales: 1,
          apuestasGanadas: 1,
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

    it('should delete a CuentaUsuario', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCuentaUsuarioToCollectionIfMissing', () => {
      it('should add a CuentaUsuario to an empty array', () => {
        const cuentaUsuario: ICuentaUsuario = { id: 123 };
        expectedResult = service.addCuentaUsuarioToCollectionIfMissing([], cuentaUsuario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cuentaUsuario);
      });

      it('should not add a CuentaUsuario to an array that contains it', () => {
        const cuentaUsuario: ICuentaUsuario = { id: 123 };
        const cuentaUsuarioCollection: ICuentaUsuario[] = [
          {
            ...cuentaUsuario,
          },
          { id: 456 },
        ];
        expectedResult = service.addCuentaUsuarioToCollectionIfMissing(cuentaUsuarioCollection, cuentaUsuario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CuentaUsuario to an array that doesn't contain it", () => {
        const cuentaUsuario: ICuentaUsuario = { id: 123 };
        const cuentaUsuarioCollection: ICuentaUsuario[] = [{ id: 456 }];
        expectedResult = service.addCuentaUsuarioToCollectionIfMissing(cuentaUsuarioCollection, cuentaUsuario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cuentaUsuario);
      });

      it('should add only unique CuentaUsuario to an array', () => {
        const cuentaUsuarioArray: ICuentaUsuario[] = [{ id: 123 }, { id: 456 }, { id: 25337 }];
        const cuentaUsuarioCollection: ICuentaUsuario[] = [{ id: 123 }];
        expectedResult = service.addCuentaUsuarioToCollectionIfMissing(cuentaUsuarioCollection, ...cuentaUsuarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cuentaUsuario: ICuentaUsuario = { id: 123 };
        const cuentaUsuario2: ICuentaUsuario = { id: 456 };
        expectedResult = service.addCuentaUsuarioToCollectionIfMissing([], cuentaUsuario, cuentaUsuario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cuentaUsuario);
        expect(expectedResult).toContain(cuentaUsuario2);
      });

      it('should accept null and undefined values', () => {
        const cuentaUsuario: ICuentaUsuario = { id: 123 };
        expectedResult = service.addCuentaUsuarioToCollectionIfMissing([], null, cuentaUsuario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cuentaUsuario);
      });

      it('should return initial array if no CuentaUsuario is added', () => {
        const cuentaUsuarioCollection: ICuentaUsuario[] = [{ id: 123 }];
        expectedResult = service.addCuentaUsuarioToCollectionIfMissing(cuentaUsuarioCollection, undefined, null);
        expect(expectedResult).toEqual(cuentaUsuarioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
