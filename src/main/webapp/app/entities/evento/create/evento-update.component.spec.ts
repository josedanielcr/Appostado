import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EventoService } from '../service/evento.service';
import { IEvento, Evento } from '../evento.model';
import { ICompetidor } from 'app/entities/competidor/competidor.model';
import { CompetidorService } from 'app/entities/competidor/service/competidor.service';
import { IDeporte } from 'app/entities/deporte/deporte.model';
import { DeporteService } from 'app/entities/deporte/service/deporte.service';
import { IDivision } from 'app/entities/division/division.model';
import { DivisionService } from 'app/entities/division/service/division.service';
import { IQuiniela } from 'app/entities/quiniela/quiniela.model';
import { QuinielaService } from 'app/entities/quiniela/service/quiniela.service';

import { EventoUpdateComponent } from './evento-update.component';

describe('Evento Management Update Component', () => {
  let comp: EventoUpdateComponent;
  let fixture: ComponentFixture<EventoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventoService: EventoService;
  let competidorService: CompetidorService;
  let deporteService: DeporteService;
  let divisionService: DivisionService;
  let quinielaService: QuinielaService;

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
    competidorService = TestBed.inject(CompetidorService);
    deporteService = TestBed.inject(DeporteService);
    divisionService = TestBed.inject(DivisionService);
    quinielaService = TestBed.inject(QuinielaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Competidor query and add missing value', () => {
      const evento: IEvento = { id: 456 };
      const ganador: ICompetidor = { id: 26116 };
      evento.ganador = ganador;
      const competidor1: ICompetidor = { id: 58519 };
      evento.competidor1 = competidor1;
      const competidor2: ICompetidor = { id: 28319 };
      evento.competidor2 = competidor2;

      const competidorCollection: ICompetidor[] = [{ id: 83516 }];
      jest.spyOn(competidorService, 'query').mockReturnValue(of(new HttpResponse({ body: competidorCollection })));
      const additionalCompetidors = [ganador, competidor1, competidor2];
      const expectedCollection: ICompetidor[] = [...additionalCompetidors, ...competidorCollection];
      jest.spyOn(competidorService, 'addCompetidorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(competidorService.query).toHaveBeenCalled();
      expect(competidorService.addCompetidorToCollectionIfMissing).toHaveBeenCalledWith(competidorCollection, ...additionalCompetidors);
      expect(comp.competidorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Deporte query and add missing value', () => {
      const evento: IEvento = { id: 456 };
      const deporte: IDeporte = { id: 55463 };
      evento.deporte = deporte;

      const deporteCollection: IDeporte[] = [{ id: 37802 }];
      jest.spyOn(deporteService, 'query').mockReturnValue(of(new HttpResponse({ body: deporteCollection })));
      const additionalDeportes = [deporte];
      const expectedCollection: IDeporte[] = [...additionalDeportes, ...deporteCollection];
      jest.spyOn(deporteService, 'addDeporteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(deporteService.query).toHaveBeenCalled();
      expect(deporteService.addDeporteToCollectionIfMissing).toHaveBeenCalledWith(deporteCollection, ...additionalDeportes);
      expect(comp.deportesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Division query and add missing value', () => {
      const evento: IEvento = { id: 456 };
      const division: IDivision = { id: 57042 };
      evento.division = division;

      const divisionCollection: IDivision[] = [{ id: 4223 }];
      jest.spyOn(divisionService, 'query').mockReturnValue(of(new HttpResponse({ body: divisionCollection })));
      const additionalDivisions = [division];
      const expectedCollection: IDivision[] = [...additionalDivisions, ...divisionCollection];
      jest.spyOn(divisionService, 'addDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(divisionService.query).toHaveBeenCalled();
      expect(divisionService.addDivisionToCollectionIfMissing).toHaveBeenCalledWith(divisionCollection, ...additionalDivisions);
      expect(comp.divisionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Quiniela query and add missing value', () => {
      const evento: IEvento = { id: 456 };
      const quiniela: IQuiniela = { id: 32928 };
      evento.quiniela = quiniela;

      const quinielaCollection: IQuiniela[] = [{ id: 87571 }];
      jest.spyOn(quinielaService, 'query').mockReturnValue(of(new HttpResponse({ body: quinielaCollection })));
      const additionalQuinielas = [quiniela];
      const expectedCollection: IQuiniela[] = [...additionalQuinielas, ...quinielaCollection];
      jest.spyOn(quinielaService, 'addQuinielaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(quinielaService.query).toHaveBeenCalled();
      expect(quinielaService.addQuinielaToCollectionIfMissing).toHaveBeenCalledWith(quinielaCollection, ...additionalQuinielas);
      expect(comp.quinielasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const evento: IEvento = { id: 456 };
      const ganador: ICompetidor = { id: 67082 };
      evento.ganador = ganador;
      const competidor1: ICompetidor = { id: 25226 };
      evento.competidor1 = competidor1;
      const competidor2: ICompetidor = { id: 93371 };
      evento.competidor2 = competidor2;
      const deporte: IDeporte = { id: 49891 };
      evento.deporte = deporte;
      const division: IDivision = { id: 44317 };
      evento.division = division;
      const quiniela: IQuiniela = { id: 93061 };
      evento.quiniela = quiniela;

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(evento));
      expect(comp.competidorsSharedCollection).toContain(ganador);
      expect(comp.competidorsSharedCollection).toContain(competidor1);
      expect(comp.competidorsSharedCollection).toContain(competidor2);
      expect(comp.deportesSharedCollection).toContain(deporte);
      expect(comp.divisionsSharedCollection).toContain(division);
      expect(comp.quinielasSharedCollection).toContain(quiniela);
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
    describe('trackCompetidorById', () => {
      it('Should return tracked Competidor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompetidorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

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

    describe('trackQuinielaById', () => {
      it('Should return tracked Quiniela primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackQuinielaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
