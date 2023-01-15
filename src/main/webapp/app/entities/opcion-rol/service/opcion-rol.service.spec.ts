import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOpcionRol, OpcionRol } from '../opcion-rol.model';

import { OpcionRolService } from './opcion-rol.service';

describe('OpcionRol Service', () => {
  let service: OpcionRolService;
  let httpMock: HttpTestingController;
  let elemDefault: IOpcionRol;
  let expectedResult: IOpcionRol | IOpcionRol[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OpcionRolService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      opcion: 'AAAAAAA',
      path: 'AAAAAAA',
      icono: 'AAAAAAA',
      nombre: 'AAAAAAA',
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

    it('should create a OpcionRol', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OpcionRol()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OpcionRol', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          opcion: 'BBBBBB',
          path: 'BBBBBB',
          icono: 'BBBBBB',
          nombre: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OpcionRol', () => {
      const patchObject = Object.assign(
        {
          opcion: 'BBBBBB',
          path: 'BBBBBB',
          icono: 'BBBBBB',
          nombre: 'BBBBBB',
        },
        new OpcionRol()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OpcionRol', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          opcion: 'BBBBBB',
          path: 'BBBBBB',
          icono: 'BBBBBB',
          nombre: 'BBBBBB',
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

    it('should delete a OpcionRol', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOpcionRolToCollectionIfMissing', () => {
      it('should add a OpcionRol to an empty array', () => {
        const opcionRol: IOpcionRol = { id: 123 };
        expectedResult = service.addOpcionRolToCollectionIfMissing([], opcionRol);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(opcionRol);
      });

      it('should not add a OpcionRol to an array that contains it', () => {
        const opcionRol: IOpcionRol = { id: 123 };
        const opcionRolCollection: IOpcionRol[] = [
          {
            ...opcionRol,
          },
          { id: 456 },
        ];
        expectedResult = service.addOpcionRolToCollectionIfMissing(opcionRolCollection, opcionRol);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OpcionRol to an array that doesn't contain it", () => {
        const opcionRol: IOpcionRol = { id: 123 };
        const opcionRolCollection: IOpcionRol[] = [{ id: 456 }];
        expectedResult = service.addOpcionRolToCollectionIfMissing(opcionRolCollection, opcionRol);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(opcionRol);
      });

      it('should add only unique OpcionRol to an array', () => {
        const opcionRolArray: IOpcionRol[] = [{ id: 123 }, { id: 456 }, { id: 7242 }];
        const opcionRolCollection: IOpcionRol[] = [{ id: 123 }];
        expectedResult = service.addOpcionRolToCollectionIfMissing(opcionRolCollection, ...opcionRolArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const opcionRol: IOpcionRol = { id: 123 };
        const opcionRol2: IOpcionRol = { id: 456 };
        expectedResult = service.addOpcionRolToCollectionIfMissing([], opcionRol, opcionRol2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(opcionRol);
        expect(expectedResult).toContain(opcionRol2);
      });

      it('should accept null and undefined values', () => {
        const opcionRol: IOpcionRol = { id: 123 };
        expectedResult = service.addOpcionRolToCollectionIfMissing([], null, opcionRol, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(opcionRol);
      });

      it('should return initial array if no OpcionRol is added', () => {
        const opcionRolCollection: IOpcionRol[] = [{ id: 123 }];
        expectedResult = service.addOpcionRolToCollectionIfMissing(opcionRolCollection, undefined, null);
        expect(expectedResult).toEqual(opcionRolCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
