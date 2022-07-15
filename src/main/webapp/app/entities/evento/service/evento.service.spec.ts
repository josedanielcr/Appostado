import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEvento, Evento } from '../evento.model';

import { EventoService } from './evento.service';

describe('Evento Service', () => {
  let service: EventoService;
  let httpMock: HttpTestingController;
  let elemDefault: IEvento;
  let expectedResult: IEvento | IEvento[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EventoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      idDeporte: 0,
      idDivision: 0,
      idCompetidor1: 0,
      idCompetidor2: 0,
      idQuiniela: 0,
      idGanador: 0,
      marcador1: 0,
      marcador2: 0,
      estado: 'AAAAAAA',
      multiplicador: 0,
      fecha: currentDate,
      horaInicio: currentDate,
      horaFin: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fecha: currentDate.format(DATE_FORMAT),
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Evento', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fecha: currentDate.format(DATE_FORMAT),
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
          horaInicio: currentDate,
          horaFin: currentDate,
        },
        returnedFromService
      );

      service.create(new Evento()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Evento', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idDeporte: 1,
          idDivision: 1,
          idCompetidor1: 1,
          idCompetidor2: 1,
          idQuiniela: 1,
          idGanador: 1,
          marcador1: 1,
          marcador2: 1,
          estado: 'BBBBBB',
          multiplicador: 1,
          fecha: currentDate.format(DATE_FORMAT),
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
          horaInicio: currentDate,
          horaFin: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Evento', () => {
      const patchObject = Object.assign(
        {
          idCompetidor2: 1,
          idGanador: 1,
          estado: 'BBBBBB',
          multiplicador: 1,
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
        },
        new Evento()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fecha: currentDate,
          horaInicio: currentDate,
          horaFin: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Evento', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idDeporte: 1,
          idDivision: 1,
          idCompetidor1: 1,
          idCompetidor2: 1,
          idQuiniela: 1,
          idGanador: 1,
          marcador1: 1,
          marcador2: 1,
          estado: 'BBBBBB',
          multiplicador: 1,
          fecha: currentDate.format(DATE_FORMAT),
          horaInicio: currentDate.format(DATE_TIME_FORMAT),
          horaFin: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
          horaInicio: currentDate,
          horaFin: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Evento', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEventoToCollectionIfMissing', () => {
      it('should add a Evento to an empty array', () => {
        const evento: IEvento = { id: 123 };
        expectedResult = service.addEventoToCollectionIfMissing([], evento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evento);
      });

      it('should not add a Evento to an array that contains it', () => {
        const evento: IEvento = { id: 123 };
        const eventoCollection: IEvento[] = [
          {
            ...evento,
          },
          { id: 456 },
        ];
        expectedResult = service.addEventoToCollectionIfMissing(eventoCollection, evento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Evento to an array that doesn't contain it", () => {
        const evento: IEvento = { id: 123 };
        const eventoCollection: IEvento[] = [{ id: 456 }];
        expectedResult = service.addEventoToCollectionIfMissing(eventoCollection, evento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evento);
      });

      it('should add only unique Evento to an array', () => {
        const eventoArray: IEvento[] = [{ id: 123 }, { id: 456 }, { id: 78202 }];
        const eventoCollection: IEvento[] = [{ id: 123 }];
        expectedResult = service.addEventoToCollectionIfMissing(eventoCollection, ...eventoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evento: IEvento = { id: 123 };
        const evento2: IEvento = { id: 456 };
        expectedResult = service.addEventoToCollectionIfMissing([], evento, evento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evento);
        expect(expectedResult).toContain(evento2);
      });

      it('should accept null and undefined values', () => {
        const evento: IEvento = { id: 123 };
        expectedResult = service.addEventoToCollectionIfMissing([], null, evento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evento);
      });

      it('should return initial array if no Evento is added', () => {
        const eventoCollection: IEvento[] = [{ id: 123 }];
        expectedResult = service.addEventoToCollectionIfMissing(eventoCollection, undefined, null);
        expect(expectedResult).toEqual(eventoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
