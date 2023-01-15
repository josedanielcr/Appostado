import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CuentaUsuarioService } from '../service/cuenta-usuario.service';
import { ICuentaUsuario, CuentaUsuario } from '../cuenta-usuario.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { CuentaUsuarioUpdateComponent } from './cuenta-usuario-update.component';

describe('CuentaUsuario Management Update Component', () => {
  let comp: CuentaUsuarioUpdateComponent;
  let fixture: ComponentFixture<CuentaUsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cuentaUsuarioService: CuentaUsuarioService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CuentaUsuarioUpdateComponent],
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
      .overrideTemplate(CuentaUsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CuentaUsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cuentaUsuarioService = TestBed.inject(CuentaUsuarioService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call usuario query and add missing value', () => {
      const cuentaUsuario: ICuentaUsuario = { id: 456 };
      const usuario: IUsuario = { id: 60412 };
      cuentaUsuario.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 59058 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const expectedCollection: IUsuario[] = [usuario, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cuentaUsuario });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, usuario);
      expect(comp.usuariosCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cuentaUsuario: ICuentaUsuario = { id: 456 };
      const usuario: IUsuario = { id: 17032 };
      cuentaUsuario.usuario = usuario;

      activatedRoute.data = of({ cuentaUsuario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cuentaUsuario));
      expect(comp.usuariosCollection).toContain(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CuentaUsuario>>();
      const cuentaUsuario = { id: 123 };
      jest.spyOn(cuentaUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuentaUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cuentaUsuario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cuentaUsuarioService.update).toHaveBeenCalledWith(cuentaUsuario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CuentaUsuario>>();
      const cuentaUsuario = new CuentaUsuario();
      jest.spyOn(cuentaUsuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuentaUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cuentaUsuario }));
      saveSubject.complete();

      // THEN
      expect(cuentaUsuarioService.create).toHaveBeenCalledWith(cuentaUsuario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CuentaUsuario>>();
      const cuentaUsuario = { id: 123 };
      jest.spyOn(cuentaUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuentaUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cuentaUsuarioService.update).toHaveBeenCalledWith(cuentaUsuario);
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
  });
});
