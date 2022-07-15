import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApuesta, Apuesta } from '../apuesta.model';

import { ApuestaService } from './apuesta.service';

describe('Apuesta Service', () => {
  let service: ApuestaService;
  let httpMock: HttpTestingController;
  let elemDefault: IApuesta;
  let expectedResult: IApuesta | IApuesta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApuestaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idUsuario: 0,
      idApostado: 0,
      idEvento: 0,
      creditosApostados: 0,
      haGanado: false,
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

    it('should create a Apuesta', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Apuesta()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Apuesta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idUsuario: 1,
          idApostado: 1,
          idEvento: 1,
          creditosApostados: 1,
          haGanado: true,
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

    it('should partial update a Apuesta', () => {
      const patchObject = Object.assign(
        {
          idUsuario: 1,
        },
        new Apuesta()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Apuesta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idUsuario: 1,
          idApostado: 1,
          idEvento: 1,
          creditosApostados: 1,
          haGanado: true,
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

    it('should delete a Apuesta', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addApuestaToCollectionIfMissing', () => {
      it('should add a Apuesta to an empty array', () => {
        const apuesta: IApuesta = { id: 123 };
        expectedResult = service.addApuestaToCollectionIfMissing([], apuesta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apuesta);
      });

      it('should not add a Apuesta to an array that contains it', () => {
        const apuesta: IApuesta = { id: 123 };
        const apuestaCollection: IApuesta[] = [
          {
            ...apuesta,
          },
          { id: 456 },
        ];
        expectedResult = service.addApuestaToCollectionIfMissing(apuestaCollection, apuesta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Apuesta to an array that doesn't contain it", () => {
        const apuesta: IApuesta = { id: 123 };
        const apuestaCollection: IApuesta[] = [{ id: 456 }];
        expectedResult = service.addApuestaToCollectionIfMissing(apuestaCollection, apuesta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apuesta);
      });

      it('should add only unique Apuesta to an array', () => {
        const apuestaArray: IApuesta[] = [{ id: 123 }, { id: 456 }, { id: 43486 }];
        const apuestaCollection: IApuesta[] = [{ id: 123 }];
        expectedResult = service.addApuestaToCollectionIfMissing(apuestaCollection, ...apuestaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const apuesta: IApuesta = { id: 123 };
        const apuesta2: IApuesta = { id: 456 };
        expectedResult = service.addApuestaToCollectionIfMissing([], apuesta, apuesta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apuesta);
        expect(expectedResult).toContain(apuesta2);
      });

      it('should accept null and undefined values', () => {
        const apuesta: IApuesta = { id: 123 };
        expectedResult = service.addApuestaToCollectionIfMissing([], null, apuesta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apuesta);
      });

      it('should return initial array if no Apuesta is added', () => {
        const apuestaCollection: IApuesta[] = [{ id: 123 }];
        expectedResult = service.addApuestaToCollectionIfMissing(apuestaCollection, undefined, null);
        expect(expectedResult).toEqual(apuestaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
