import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductoUsuario, ProductoUsuario } from '../producto-usuario.model';

import { ProductoUsuarioService } from './producto-usuario.service';

describe('ProductoUsuario Service', () => {
  let service: ProductoUsuarioService;
  let httpMock: HttpTestingController;
  let elemDefault: IProductoUsuario;
  let expectedResult: IProductoUsuario | IProductoUsuario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductoUsuarioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      reclamado: false,
      codigo: 'AAAAAAA',
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

    it('should create a ProductoUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ProductoUsuario()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductoUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reclamado: true,
          codigo: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductoUsuario', () => {
      const patchObject = Object.assign(
        {
          codigo: 'BBBBBB',
        },
        new ProductoUsuario()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductoUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reclamado: true,
          codigo: 'BBBBBB',
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

    it('should delete a ProductoUsuario', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProductoUsuarioToCollectionIfMissing', () => {
      it('should add a ProductoUsuario to an empty array', () => {
        const productoUsuario: IProductoUsuario = { id: 123 };
        expectedResult = service.addProductoUsuarioToCollectionIfMissing([], productoUsuario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productoUsuario);
      });

      it('should not add a ProductoUsuario to an array that contains it', () => {
        const productoUsuario: IProductoUsuario = { id: 123 };
        const productoUsuarioCollection: IProductoUsuario[] = [
          {
            ...productoUsuario,
          },
          { id: 456 },
        ];
        expectedResult = service.addProductoUsuarioToCollectionIfMissing(productoUsuarioCollection, productoUsuario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductoUsuario to an array that doesn't contain it", () => {
        const productoUsuario: IProductoUsuario = { id: 123 };
        const productoUsuarioCollection: IProductoUsuario[] = [{ id: 456 }];
        expectedResult = service.addProductoUsuarioToCollectionIfMissing(productoUsuarioCollection, productoUsuario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productoUsuario);
      });

      it('should add only unique ProductoUsuario to an array', () => {
        const productoUsuarioArray: IProductoUsuario[] = [{ id: 123 }, { id: 456 }, { id: 81087 }];
        const productoUsuarioCollection: IProductoUsuario[] = [{ id: 123 }];
        expectedResult = service.addProductoUsuarioToCollectionIfMissing(productoUsuarioCollection, ...productoUsuarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productoUsuario: IProductoUsuario = { id: 123 };
        const productoUsuario2: IProductoUsuario = { id: 456 };
        expectedResult = service.addProductoUsuarioToCollectionIfMissing([], productoUsuario, productoUsuario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productoUsuario);
        expect(expectedResult).toContain(productoUsuario2);
      });

      it('should accept null and undefined values', () => {
        const productoUsuario: IProductoUsuario = { id: 123 };
        expectedResult = service.addProductoUsuarioToCollectionIfMissing([], null, productoUsuario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productoUsuario);
      });

      it('should return initial array if no ProductoUsuario is added', () => {
        const productoUsuarioCollection: IProductoUsuario[] = [{ id: 123 }];
        expectedResult = service.addProductoUsuarioToCollectionIfMissing(productoUsuarioCollection, undefined, null);
        expect(expectedResult).toEqual(productoUsuarioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
