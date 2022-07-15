import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApuestaTransaccionService } from '../service/apuesta-transaccion.service';
import { IApuestaTransaccion, ApuestaTransaccion } from '../apuesta-transaccion.model';
import { IApuesta } from 'app/entities/apuesta/apuesta.model';
import { ApuestaService } from 'app/entities/apuesta/service/apuesta.service';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';
import { TransaccionService } from 'app/entities/transaccion/service/transaccion.service';

import { ApuestaTransaccionUpdateComponent } from './apuesta-transaccion-update.component';

describe('ApuestaTransaccion Management Update Component', () => {
  let comp: ApuestaTransaccionUpdateComponent;
  let fixture: ComponentFixture<ApuestaTransaccionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apuestaTransaccionService: ApuestaTransaccionService;
  let apuestaService: ApuestaService;
  let transaccionService: TransaccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ApuestaTransaccionUpdateComponent],
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
      .overrideTemplate(ApuestaTransaccionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApuestaTransaccionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apuestaTransaccionService = TestBed.inject(ApuestaTransaccionService);
    apuestaService = TestBed.inject(ApuestaService);
    transaccionService = TestBed.inject(TransaccionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Apuesta query and add missing value', () => {
      const apuestaTransaccion: IApuestaTransaccion = { id: 456 };
      const apuesta: IApuesta = { id: 69544 };
      apuestaTransaccion.apuesta = apuesta;

      const apuestaCollection: IApuesta[] = [{ id: 40476 }];
      jest.spyOn(apuestaService, 'query').mockReturnValue(of(new HttpResponse({ body: apuestaCollection })));
      const additionalApuestas = [apuesta];
      const expectedCollection: IApuesta[] = [...additionalApuestas, ...apuestaCollection];
      jest.spyOn(apuestaService, 'addApuestaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apuestaTransaccion });
      comp.ngOnInit();

      expect(apuestaService.query).toHaveBeenCalled();
      expect(apuestaService.addApuestaToCollectionIfMissing).toHaveBeenCalledWith(apuestaCollection, ...additionalApuestas);
      expect(comp.apuestasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Transaccion query and add missing value', () => {
      const apuestaTransaccion: IApuestaTransaccion = { id: 456 };
      const transaccion: ITransaccion = { id: 56461 };
      apuestaTransaccion.transaccion = transaccion;

      const transaccionCollection: ITransaccion[] = [{ id: 90883 }];
      jest.spyOn(transaccionService, 'query').mockReturnValue(of(new HttpResponse({ body: transaccionCollection })));
      const additionalTransaccions = [transaccion];
      const expectedCollection: ITransaccion[] = [...additionalTransaccions, ...transaccionCollection];
      jest.spyOn(transaccionService, 'addTransaccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apuestaTransaccion });
      comp.ngOnInit();

      expect(transaccionService.query).toHaveBeenCalled();
      expect(transaccionService.addTransaccionToCollectionIfMissing).toHaveBeenCalledWith(transaccionCollection, ...additionalTransaccions);
      expect(comp.transaccionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const apuestaTransaccion: IApuestaTransaccion = { id: 456 };
      const apuesta: IApuesta = { id: 97412 };
      apuestaTransaccion.apuesta = apuesta;
      const transaccion: ITransaccion = { id: 20996 };
      apuestaTransaccion.transaccion = transaccion;

      activatedRoute.data = of({ apuestaTransaccion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(apuestaTransaccion));
      expect(comp.apuestasSharedCollection).toContain(apuesta);
      expect(comp.transaccionsSharedCollection).toContain(transaccion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApuestaTransaccion>>();
      const apuestaTransaccion = { id: 123 };
      jest.spyOn(apuestaTransaccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apuestaTransaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apuestaTransaccion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(apuestaTransaccionService.update).toHaveBeenCalledWith(apuestaTransaccion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApuestaTransaccion>>();
      const apuestaTransaccion = new ApuestaTransaccion();
      jest.spyOn(apuestaTransaccionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apuestaTransaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apuestaTransaccion }));
      saveSubject.complete();

      // THEN
      expect(apuestaTransaccionService.create).toHaveBeenCalledWith(apuestaTransaccion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApuestaTransaccion>>();
      const apuestaTransaccion = { id: 123 };
      jest.spyOn(apuestaTransaccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apuestaTransaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apuestaTransaccionService.update).toHaveBeenCalledWith(apuestaTransaccion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackApuestaById', () => {
      it('Should return tracked Apuesta primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApuestaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTransaccionById', () => {
      it('Should return tracked Transaccion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransaccionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
