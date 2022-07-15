import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILigaUsuario, LigaUsuario } from '../liga-usuario.model';

import { LigaUsuarioService } from './liga-usuario.service';

describe('LigaUsuario Service', () => {
  let service: LigaUsuarioService;
  let httpMock: HttpTestingController;
  let elemDefault: ILigaUsuario;
  let expectedResult: ILigaUsuario | ILigaUsuario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LigaUsuarioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idUsuario: 0,
      idLiga: 0,
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

    it('should create a LigaUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LigaUsuario()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LigaUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idUsuario: 1,
          idLiga: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LigaUsuario', () => {
      const patchObject = Object.assign(
        {
          idUsuario: 1,
          idLiga: 1,
        },
        new LigaUsuario()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LigaUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idUsuario: 1,
          idLiga: 1,
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

    it('should delete a LigaUsuario', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLigaUsuarioToCollectionIfMissing', () => {
      it('should add a LigaUsuario to an empty array', () => {
        const ligaUsuario: ILigaUsuario = { id: 123 };
        expectedResult = service.addLigaUsuarioToCollectionIfMissing([], ligaUsuario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ligaUsuario);
      });

      it('should not add a LigaUsuario to an array that contains it', () => {
        const ligaUsuario: ILigaUsuario = { id: 123 };
        const ligaUsuarioCollection: ILigaUsuario[] = [
          {
            ...ligaUsuario,
          },
          { id: 456 },
        ];
        expectedResult = service.addLigaUsuarioToCollectionIfMissing(ligaUsuarioCollection, ligaUsuario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LigaUsuario to an array that doesn't contain it", () => {
        const ligaUsuario: ILigaUsuario = { id: 123 };
        const ligaUsuarioCollection: ILigaUsuario[] = [{ id: 456 }];
        expectedResult = service.addLigaUsuarioToCollectionIfMissing(ligaUsuarioCollection, ligaUsuario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ligaUsuario);
      });

      it('should add only unique LigaUsuario to an array', () => {
        const ligaUsuarioArray: ILigaUsuario[] = [{ id: 123 }, { id: 456 }, { id: 59895 }];
        const ligaUsuarioCollection: ILigaUsuario[] = [{ id: 123 }];
        expectedResult = service.addLigaUsuarioToCollectionIfMissing(ligaUsuarioCollection, ...ligaUsuarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ligaUsuario: ILigaUsuario = { id: 123 };
        const ligaUsuario2: ILigaUsuario = { id: 456 };
        expectedResult = service.addLigaUsuarioToCollectionIfMissing([], ligaUsuario, ligaUsuario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ligaUsuario);
        expect(expectedResult).toContain(ligaUsuario2);
      });

      it('should accept null and undefined values', () => {
        const ligaUsuario: ILigaUsuario = { id: 123 };
        expectedResult = service.addLigaUsuarioToCollectionIfMissing([], null, ligaUsuario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ligaUsuario);
      });

      it('should return initial array if no LigaUsuario is added', () => {
        const ligaUsuarioCollection: ILigaUsuario[] = [{ id: 123 }];
        expectedResult = service.addLigaUsuarioToCollectionIfMissing(ligaUsuarioCollection, undefined, null);
        expect(expectedResult).toEqual(ligaUsuarioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
