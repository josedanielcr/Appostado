import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CuentaUsuarioService } from '../service/cuenta-usuario.service';
import { ICuentaUsuario, CuentaUsuario } from '../cuenta-usuario.model';

import { CuentaUsuarioUpdateComponent } from './cuenta-usuario-update.component';

describe('CuentaUsuario Management Update Component', () => {
  let comp: CuentaUsuarioUpdateComponent;
  let fixture: ComponentFixture<CuentaUsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cuentaUsuarioService: CuentaUsuarioService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cuentaUsuario: ICuentaUsuario = { id: 456 };

      activatedRoute.data = of({ cuentaUsuario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cuentaUsuario));
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
});
