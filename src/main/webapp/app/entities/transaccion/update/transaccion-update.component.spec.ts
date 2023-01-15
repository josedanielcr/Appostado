import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TransaccionService } from '../service/transaccion.service';
import { ITransaccion, Transaccion } from '../transaccion.model';
import { ICuentaUsuario } from 'app/entities/cuenta-usuario/cuenta-usuario.model';
import { CuentaUsuarioService } from 'app/entities/cuenta-usuario/service/cuenta-usuario.service';

import { TransaccionUpdateComponent } from './transaccion-update.component';

describe('Transaccion Management Update Component', () => {
  let comp: TransaccionUpdateComponent;
  let fixture: ComponentFixture<TransaccionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transaccionService: TransaccionService;
  let cuentaUsuarioService: CuentaUsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TransaccionUpdateComponent],
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
      .overrideTemplate(TransaccionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransaccionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transaccionService = TestBed.inject(TransaccionService);
    cuentaUsuarioService = TestBed.inject(CuentaUsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CuentaUsuario query and add missing value', () => {
      const transaccion: ITransaccion = { id: 456 };
      const cuenta: ICuentaUsuario = { id: 20230 };
      transaccion.cuenta = cuenta;

      const cuentaUsuarioCollection: ICuentaUsuario[] = [{ id: 14599 }];
      jest.spyOn(cuentaUsuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: cuentaUsuarioCollection })));
      const additionalCuentaUsuarios = [cuenta];
      const expectedCollection: ICuentaUsuario[] = [...additionalCuentaUsuarios, ...cuentaUsuarioCollection];
      jest.spyOn(cuentaUsuarioService, 'addCuentaUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transaccion });
      comp.ngOnInit();

      expect(cuentaUsuarioService.query).toHaveBeenCalled();
      expect(cuentaUsuarioService.addCuentaUsuarioToCollectionIfMissing).toHaveBeenCalledWith(
        cuentaUsuarioCollection,
        ...additionalCuentaUsuarios
      );
      expect(comp.cuentaUsuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transaccion: ITransaccion = { id: 456 };
      const cuenta: ICuentaUsuario = { id: 97825 };
      transaccion.cuenta = cuenta;

      activatedRoute.data = of({ transaccion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(transaccion));
      expect(comp.cuentaUsuariosSharedCollection).toContain(cuenta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Transaccion>>();
      const transaccion = { id: 123 };
      jest.spyOn(transaccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transaccion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(transaccionService.update).toHaveBeenCalledWith(transaccion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Transaccion>>();
      const transaccion = new Transaccion();
      jest.spyOn(transaccionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transaccion }));
      saveSubject.complete();

      // THEN
      expect(transaccionService.create).toHaveBeenCalledWith(transaccion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Transaccion>>();
      const transaccion = { id: 123 };
      jest.spyOn(transaccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transaccionService.update).toHaveBeenCalledWith(transaccion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCuentaUsuarioById', () => {
      it('Should return tracked CuentaUsuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCuentaUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
