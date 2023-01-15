import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MisionTransaccionService } from '../service/mision-transaccion.service';
import { IMisionTransaccion, MisionTransaccion } from '../mision-transaccion.model';
import { IMision } from 'app/entities/mision/mision.model';
import { MisionService } from 'app/entities/mision/service/mision.service';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';
import { TransaccionService } from 'app/entities/transaccion/service/transaccion.service';

import { MisionTransaccionUpdateComponent } from './mision-transaccion-update.component';

describe('MisionTransaccion Management Update Component', () => {
  let comp: MisionTransaccionUpdateComponent;
  let fixture: ComponentFixture<MisionTransaccionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let misionTransaccionService: MisionTransaccionService;
  let misionService: MisionService;
  let transaccionService: TransaccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MisionTransaccionUpdateComponent],
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
      .overrideTemplate(MisionTransaccionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MisionTransaccionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    misionTransaccionService = TestBed.inject(MisionTransaccionService);
    misionService = TestBed.inject(MisionService);
    transaccionService = TestBed.inject(TransaccionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Mision query and add missing value', () => {
      const misionTransaccion: IMisionTransaccion = { id: 456 };
      const mision: IMision = { id: 46554 };
      misionTransaccion.mision = mision;

      const misionCollection: IMision[] = [{ id: 80920 }];
      jest.spyOn(misionService, 'query').mockReturnValue(of(new HttpResponse({ body: misionCollection })));
      const additionalMisions = [mision];
      const expectedCollection: IMision[] = [...additionalMisions, ...misionCollection];
      jest.spyOn(misionService, 'addMisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ misionTransaccion });
      comp.ngOnInit();

      expect(misionService.query).toHaveBeenCalled();
      expect(misionService.addMisionToCollectionIfMissing).toHaveBeenCalledWith(misionCollection, ...additionalMisions);
      expect(comp.misionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Transaccion query and add missing value', () => {
      const misionTransaccion: IMisionTransaccion = { id: 456 };
      const transaccion: ITransaccion = { id: 43872 };
      misionTransaccion.transaccion = transaccion;

      const transaccionCollection: ITransaccion[] = [{ id: 98501 }];
      jest.spyOn(transaccionService, 'query').mockReturnValue(of(new HttpResponse({ body: transaccionCollection })));
      const additionalTransaccions = [transaccion];
      const expectedCollection: ITransaccion[] = [...additionalTransaccions, ...transaccionCollection];
      jest.spyOn(transaccionService, 'addTransaccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ misionTransaccion });
      comp.ngOnInit();

      expect(transaccionService.query).toHaveBeenCalled();
      expect(transaccionService.addTransaccionToCollectionIfMissing).toHaveBeenCalledWith(transaccionCollection, ...additionalTransaccions);
      expect(comp.transaccionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const misionTransaccion: IMisionTransaccion = { id: 456 };
      const mision: IMision = { id: 55626 };
      misionTransaccion.mision = mision;
      const transaccion: ITransaccion = { id: 29002 };
      misionTransaccion.transaccion = transaccion;

      activatedRoute.data = of({ misionTransaccion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(misionTransaccion));
      expect(comp.misionsSharedCollection).toContain(mision);
      expect(comp.transaccionsSharedCollection).toContain(transaccion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MisionTransaccion>>();
      const misionTransaccion = { id: 123 };
      jest.spyOn(misionTransaccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ misionTransaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: misionTransaccion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(misionTransaccionService.update).toHaveBeenCalledWith(misionTransaccion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MisionTransaccion>>();
      const misionTransaccion = new MisionTransaccion();
      jest.spyOn(misionTransaccionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ misionTransaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: misionTransaccion }));
      saveSubject.complete();

      // THEN
      expect(misionTransaccionService.create).toHaveBeenCalledWith(misionTransaccion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MisionTransaccion>>();
      const misionTransaccion = { id: 123 };
      jest.spyOn(misionTransaccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ misionTransaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(misionTransaccionService.update).toHaveBeenCalledWith(misionTransaccion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMisionById', () => {
      it('Should return tracked Mision primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMisionById(0, entity);
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
