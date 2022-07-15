import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CanjeService } from '../service/canje.service';
import { ICanje, Canje } from '../canje.model';
import { IPremio } from 'app/entities/premio/premio.model';
import { PremioService } from 'app/entities/premio/service/premio.service';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';
import { TransaccionService } from 'app/entities/transaccion/service/transaccion.service';

import { CanjeUpdateComponent } from './canje-update.component';

describe('Canje Management Update Component', () => {
  let comp: CanjeUpdateComponent;
  let fixture: ComponentFixture<CanjeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let canjeService: CanjeService;
  let premioService: PremioService;
  let transaccionService: TransaccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CanjeUpdateComponent],
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
      .overrideTemplate(CanjeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CanjeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    canjeService = TestBed.inject(CanjeService);
    premioService = TestBed.inject(PremioService);
    transaccionService = TestBed.inject(TransaccionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Premio query and add missing value', () => {
      const canje: ICanje = { id: 456 };
      const premio: IPremio = { id: 33248 };
      canje.premio = premio;

      const premioCollection: IPremio[] = [{ id: 68094 }];
      jest.spyOn(premioService, 'query').mockReturnValue(of(new HttpResponse({ body: premioCollection })));
      const additionalPremios = [premio];
      const expectedCollection: IPremio[] = [...additionalPremios, ...premioCollection];
      jest.spyOn(premioService, 'addPremioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ canje });
      comp.ngOnInit();

      expect(premioService.query).toHaveBeenCalled();
      expect(premioService.addPremioToCollectionIfMissing).toHaveBeenCalledWith(premioCollection, ...additionalPremios);
      expect(comp.premiosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Transaccion query and add missing value', () => {
      const canje: ICanje = { id: 456 };
      const transaccion: ITransaccion = { id: 89451 };
      canje.transaccion = transaccion;

      const transaccionCollection: ITransaccion[] = [{ id: 46258 }];
      jest.spyOn(transaccionService, 'query').mockReturnValue(of(new HttpResponse({ body: transaccionCollection })));
      const additionalTransaccions = [transaccion];
      const expectedCollection: ITransaccion[] = [...additionalTransaccions, ...transaccionCollection];
      jest.spyOn(transaccionService, 'addTransaccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ canje });
      comp.ngOnInit();

      expect(transaccionService.query).toHaveBeenCalled();
      expect(transaccionService.addTransaccionToCollectionIfMissing).toHaveBeenCalledWith(transaccionCollection, ...additionalTransaccions);
      expect(comp.transaccionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const canje: ICanje = { id: 456 };
      const premio: IPremio = { id: 64681 };
      canje.premio = premio;
      const transaccion: ITransaccion = { id: 66184 };
      canje.transaccion = transaccion;

      activatedRoute.data = of({ canje });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(canje));
      expect(comp.premiosSharedCollection).toContain(premio);
      expect(comp.transaccionsSharedCollection).toContain(transaccion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Canje>>();
      const canje = { id: 123 };
      jest.spyOn(canjeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ canje });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: canje }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(canjeService.update).toHaveBeenCalledWith(canje);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Canje>>();
      const canje = new Canje();
      jest.spyOn(canjeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ canje });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: canje }));
      saveSubject.complete();

      // THEN
      expect(canjeService.create).toHaveBeenCalledWith(canje);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Canje>>();
      const canje = { id: 123 };
      jest.spyOn(canjeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ canje });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(canjeService.update).toHaveBeenCalledWith(canje);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPremioById', () => {
      it('Should return tracked Premio primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPremioById(0, entity);
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
