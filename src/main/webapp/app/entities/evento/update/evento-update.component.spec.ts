import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EventoService } from '../service/evento.service';
import { IEvento, Evento } from '../evento.model';
import { IDeporte } from 'app/entities/deporte/deporte.model';
import { DeporteService } from 'app/entities/deporte/service/deporte.service';
import { IDivision } from 'app/entities/division/division.model';
import { DivisionService } from 'app/entities/division/service/division.service';
import { ICompetidor } from 'app/entities/competidor/competidor.model';
import { CompetidorService } from 'app/entities/competidor/service/competidor.service';

import { EventoUpdateComponent } from './evento-update.component';

describe('Evento Management Update Component', () => {
  let comp: EventoUpdateComponent;
  let fixture: ComponentFixture<EventoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventoService: EventoService;
  let deporteService: DeporteService;
  let divisionService: DivisionService;
  let competidorService: CompetidorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EventoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EventoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventoService = TestBed.inject(EventoService);
    deporteService = TestBed.inject(DeporteService);
    divisionService = TestBed.inject(DivisionService);
    competidorService = TestBed.inject(CompetidorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call deporte query and add missing value', () => {
      const evento: IEvento = { id: 456 };
      const deporte: IDeporte = { id: 55463 };
      evento.deporte = deporte;

      const deporteCollection: IDeporte[] = [{ id: 37802 }];
      jest.spyOn(deporteService, 'query').mockReturnValue(of(new HttpResponse({ body: deporteCollection })));
      const expectedCollection: IDeporte[] = [deporte, ...deporteCollection];
      jest.spyOn(deporteService, 'addDeporteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(deporteService.query).toHaveBeenCalled();
      expect(deporteService.addDeporteToCollectionIfMissing).toHaveBeenCalledWith(deporteCollection, deporte);
      expect(comp.deportesCollection).toEqual(expectedCollection);
    });

    it('Should call division query and add missing value', () => {
      const evento: IEvento = { id: 456 };
      const division: IDivision = { id: 57042 };
      evento.division = division;

      const divisionCollection: IDivision[] = [{ id: 4223 }];
      jest.spyOn(divisionService, 'query').mockReturnValue(of(new HttpResponse({ body: divisionCollection })));
      const expectedCollection: IDivision[] = [division, ...divisionCollection];
      jest.spyOn(divisionService, 'addDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(divisionService.query).toHaveBeenCalled();
      expect(divisionService.addDivisionToCollectionIfMissing).toHaveBeenCalledWith(divisionCollection, division);
      expect(comp.divisionsCollection).toEqual(expectedCollection);
    });

    it('Should call competidor1 query and add missing value', () => {
      const evento: IEvento = { id: 456 };
      const competidor1: ICompetidor = { id: 26116 };
      evento.competidor1 = competidor1;

      const competidor1Collection: ICompetidor[] = [{ id: 58519 }];
      jest.spyOn(competidorService, 'query').mockReturnValue(of(new HttpResponse({ body: competidor1Collection })));
      const expectedCollection: ICompetidor[] = [competidor1, ...competidor1Collection];
      jest.spyOn(competidorService, 'addCompetidorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(competidorService.query).toHaveBeenCalled();
      expect(competidorService.addCompetidorToCollectionIfMissing).toHaveBeenCalledWith(competidor1Collection, competidor1);
      expect(comp.competidor1sCollection).toEqual(expectedCollection);
    });

    it('Should call competidor2 query and add missing value', () => {
      const evento: IEvento = { id: 456 };
      const competidor2: ICompetidor = { id: 28319 };
      evento.competidor2 = competidor2;

      const competidor2Collection: ICompetidor[] = [{ id: 83516 }];
      jest.spyOn(competidorService, 'query').mockReturnValue(of(new HttpResponse({ body: competidor2Collection })));
      const expectedCollection: ICompetidor[] = [competidor2, ...competidor2Collection];
      jest.spyOn(competidorService, 'addCompetidorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(competidorService.query).toHaveBeenCalled();
      expect(competidorService.addCompetidorToCollectionIfMissing).toHaveBeenCalledWith(competidor2Collection, competidor2);
      expect(comp.competidor2sCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const evento: IEvento = { id: 456 };
      const deporte: IDeporte = { id: 49891 };
      evento.deporte = deporte;
      const division: IDivision = { id: 44317 };
      evento.division = division;
      const competidor1: ICompetidor = { id: 67082 };
      evento.competidor1 = competidor1;
      const competidor2: ICompetidor = { id: 25226 };
      evento.competidor2 = competidor2;

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(evento));
      expect(comp.deportesCollection).toContain(deporte);
      expect(comp.divisionsCollection).toContain(division);
      expect(comp.competidor1sCollection).toContain(competidor1);
      expect(comp.competidor2sCollection).toContain(competidor2);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Evento>>();
      const evento = { id: 123 };
      jest.spyOn(eventoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evento }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventoService.update).toHaveBeenCalledWith(evento);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Evento>>();
      const evento = new Evento();
      jest.spyOn(eventoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evento }));
      saveSubject.complete();

      // THEN
      expect(eventoService.create).toHaveBeenCalledWith(evento);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Evento>>();
      const evento = { id: 123 };
      jest.spyOn(eventoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventoService.update).toHaveBeenCalledWith(evento);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDeporteById', () => {
      it('Should return tracked Deporte primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDeporteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDivisionById', () => {
      it('Should return tracked Division primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDivisionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCompetidorById', () => {
      it('Should return tracked Competidor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompetidorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
