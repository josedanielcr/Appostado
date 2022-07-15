import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LigaUsuarioService } from '../service/liga-usuario.service';
import { ILigaUsuario, LigaUsuario } from '../liga-usuario.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { ILiga } from 'app/entities/liga/liga.model';
import { LigaService } from 'app/entities/liga/service/liga.service';

import { LigaUsuarioUpdateComponent } from './liga-usuario-update.component';

describe('LigaUsuario Management Update Component', () => {
  let comp: LigaUsuarioUpdateComponent;
  let fixture: ComponentFixture<LigaUsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ligaUsuarioService: LigaUsuarioService;
  let usuarioService: UsuarioService;
  let ligaService: LigaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LigaUsuarioUpdateComponent],
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
      .overrideTemplate(LigaUsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LigaUsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ligaUsuarioService = TestBed.inject(LigaUsuarioService);
    usuarioService = TestBed.inject(UsuarioService);
    ligaService = TestBed.inject(LigaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const ligaUsuario: ILigaUsuario = { id: 456 };
      const usuario: IUsuario = { id: 36263 };
      ligaUsuario.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 64743 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ligaUsuario });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Liga query and add missing value', () => {
      const ligaUsuario: ILigaUsuario = { id: 456 };
      const liga: ILiga = { id: 86913 };
      ligaUsuario.liga = liga;

      const ligaCollection: ILiga[] = [{ id: 65409 }];
      jest.spyOn(ligaService, 'query').mockReturnValue(of(new HttpResponse({ body: ligaCollection })));
      const additionalLigas = [liga];
      const expectedCollection: ILiga[] = [...additionalLigas, ...ligaCollection];
      jest.spyOn(ligaService, 'addLigaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ligaUsuario });
      comp.ngOnInit();

      expect(ligaService.query).toHaveBeenCalled();
      expect(ligaService.addLigaToCollectionIfMissing).toHaveBeenCalledWith(ligaCollection, ...additionalLigas);
      expect(comp.ligasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ligaUsuario: ILigaUsuario = { id: 456 };
      const usuario: IUsuario = { id: 39374 };
      ligaUsuario.usuario = usuario;
      const liga: ILiga = { id: 38048 };
      ligaUsuario.liga = liga;

      activatedRoute.data = of({ ligaUsuario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ligaUsuario));
      expect(comp.usuariosSharedCollection).toContain(usuario);
      expect(comp.ligasSharedCollection).toContain(liga);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LigaUsuario>>();
      const ligaUsuario = { id: 123 };
      jest.spyOn(ligaUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligaUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ligaUsuario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ligaUsuarioService.update).toHaveBeenCalledWith(ligaUsuario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LigaUsuario>>();
      const ligaUsuario = new LigaUsuario();
      jest.spyOn(ligaUsuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligaUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ligaUsuario }));
      saveSubject.complete();

      // THEN
      expect(ligaUsuarioService.create).toHaveBeenCalledWith(ligaUsuario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<LigaUsuario>>();
      const ligaUsuario = { id: 123 };
      jest.spyOn(ligaUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligaUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ligaUsuarioService.update).toHaveBeenCalledWith(ligaUsuario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLigaById', () => {
      it('Should return tracked Liga primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLigaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
