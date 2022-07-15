import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MisionUsuarioService } from '../service/mision-usuario.service';
import { IMisionUsuario, MisionUsuario } from '../mision-usuario.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IMision } from 'app/entities/mision/mision.model';
import { MisionService } from 'app/entities/mision/service/mision.service';

import { MisionUsuarioUpdateComponent } from './mision-usuario-update.component';

describe('MisionUsuario Management Update Component', () => {
  let comp: MisionUsuarioUpdateComponent;
  let fixture: ComponentFixture<MisionUsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let misionUsuarioService: MisionUsuarioService;
  let usuarioService: UsuarioService;
  let misionService: MisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MisionUsuarioUpdateComponent],
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
      .overrideTemplate(MisionUsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MisionUsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    misionUsuarioService = TestBed.inject(MisionUsuarioService);
    usuarioService = TestBed.inject(UsuarioService);
    misionService = TestBed.inject(MisionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const misionUsuario: IMisionUsuario = { id: 456 };
      const usuario: IUsuario = { id: 78766 };
      misionUsuario.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 7218 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ misionUsuario });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Mision query and add missing value', () => {
      const misionUsuario: IMisionUsuario = { id: 456 };
      const mision: IMision = { id: 85762 };
      misionUsuario.mision = mision;

      const misionCollection: IMision[] = [{ id: 46997 }];
      jest.spyOn(misionService, 'query').mockReturnValue(of(new HttpResponse({ body: misionCollection })));
      const additionalMisions = [mision];
      const expectedCollection: IMision[] = [...additionalMisions, ...misionCollection];
      jest.spyOn(misionService, 'addMisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ misionUsuario });
      comp.ngOnInit();

      expect(misionService.query).toHaveBeenCalled();
      expect(misionService.addMisionToCollectionIfMissing).toHaveBeenCalledWith(misionCollection, ...additionalMisions);
      expect(comp.misionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const misionUsuario: IMisionUsuario = { id: 456 };
      const usuario: IUsuario = { id: 87253 };
      misionUsuario.usuario = usuario;
      const mision: IMision = { id: 40047 };
      misionUsuario.mision = mision;

      activatedRoute.data = of({ misionUsuario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(misionUsuario));
      expect(comp.usuariosSharedCollection).toContain(usuario);
      expect(comp.misionsSharedCollection).toContain(mision);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MisionUsuario>>();
      const misionUsuario = { id: 123 };
      jest.spyOn(misionUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ misionUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: misionUsuario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(misionUsuarioService.update).toHaveBeenCalledWith(misionUsuario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MisionUsuario>>();
      const misionUsuario = new MisionUsuario();
      jest.spyOn(misionUsuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ misionUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: misionUsuario }));
      saveSubject.complete();

      // THEN
      expect(misionUsuarioService.create).toHaveBeenCalledWith(misionUsuario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MisionUsuario>>();
      const misionUsuario = { id: 123 };
      jest.spyOn(misionUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ misionUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(misionUsuarioService.update).toHaveBeenCalledWith(misionUsuario);
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

    describe('trackMisionById', () => {
      it('Should return tracked Mision primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMisionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
