import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApuestaService } from '../service/apuesta.service';
import { IApuesta, Apuesta } from '../apuesta.model';
import { ICompetidor } from 'app/entities/competidor/competidor.model';
import { CompetidorService } from 'app/entities/competidor/service/competidor.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IEvento } from 'app/entities/evento/evento.model';
import { EventoService } from 'app/entities/evento/service/evento.service';

import { ApuestaUpdateComponent } from './apuesta-update.component';

describe('Apuesta Management Update Component', () => {
  let comp: ApuestaUpdateComponent;
  let fixture: ComponentFixture<ApuestaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apuestaService: ApuestaService;
  let competidorService: CompetidorService;
  let usuarioService: UsuarioService;
  let eventoService: EventoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ApuestaUpdateComponent],
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
      .overrideTemplate(ApuestaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApuestaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apuestaService = TestBed.inject(ApuestaService);
    competidorService = TestBed.inject(CompetidorService);
    usuarioService = TestBed.inject(UsuarioService);
    eventoService = TestBed.inject(EventoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call apostado query and add missing value', () => {
      const apuesta: IApuesta = { id: 456 };
      const apostado: ICompetidor = { id: 8358 };
      apuesta.apostado = apostado;

      const apostadoCollection: ICompetidor[] = [{ id: 67057 }];
      jest.spyOn(competidorService, 'query').mockReturnValue(of(new HttpResponse({ body: apostadoCollection })));
      const expectedCollection: ICompetidor[] = [apostado, ...apostadoCollection];
      jest.spyOn(competidorService, 'addCompetidorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apuesta });
      comp.ngOnInit();

      expect(competidorService.query).toHaveBeenCalled();
      expect(competidorService.addCompetidorToCollectionIfMissing).toHaveBeenCalledWith(apostadoCollection, apostado);
      expect(comp.apostadosCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const apuesta: IApuesta = { id: 456 };
      const usuario: IUsuario = { id: 26476 };
      apuesta.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 35902 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apuesta });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Evento query and add missing value', () => {
      const apuesta: IApuesta = { id: 456 };
      const evento: IEvento = { id: 64430 };
      apuesta.evento = evento;

      const eventoCollection: IEvento[] = [{ id: 2901 }];
      jest.spyOn(eventoService, 'query').mockReturnValue(of(new HttpResponse({ body: eventoCollection })));
      const additionalEventos = [evento];
      const expectedCollection: IEvento[] = [...additionalEventos, ...eventoCollection];
      jest.spyOn(eventoService, 'addEventoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apuesta });
      comp.ngOnInit();

      expect(eventoService.query).toHaveBeenCalled();
      expect(eventoService.addEventoToCollectionIfMissing).toHaveBeenCalledWith(eventoCollection, ...additionalEventos);
      expect(comp.eventosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const apuesta: IApuesta = { id: 456 };
      const apostado: ICompetidor = { id: 40121 };
      apuesta.apostado = apostado;
      const usuario: IUsuario = { id: 8145 };
      apuesta.usuario = usuario;
      const evento: IEvento = { id: 77465 };
      apuesta.evento = evento;

      activatedRoute.data = of({ apuesta });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(apuesta));
      expect(comp.apostadosCollection).toContain(apostado);
      expect(comp.usuariosSharedCollection).toContain(usuario);
      expect(comp.eventosSharedCollection).toContain(evento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apuesta>>();
      const apuesta = { id: 123 };
      jest.spyOn(apuestaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apuesta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apuesta }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(apuestaService.update).toHaveBeenCalledWith(apuesta);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apuesta>>();
      const apuesta = new Apuesta();
      jest.spyOn(apuestaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apuesta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apuesta }));
      saveSubject.complete();

      // THEN
      expect(apuestaService.create).toHaveBeenCalledWith(apuesta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apuesta>>();
      const apuesta = { id: 123 };
      jest.spyOn(apuestaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apuesta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apuestaService.update).toHaveBeenCalledWith(apuesta);
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

    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEventoById', () => {
      it('Should return tracked Evento primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEventoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
