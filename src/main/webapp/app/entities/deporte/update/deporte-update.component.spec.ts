import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeporteService } from '../service/deporte.service';
import { IDeporte, Deporte } from '../deporte.model';

import { DeporteUpdateComponent } from './deporte-update.component';

describe('Deporte Management Update Component', () => {
  let comp: DeporteUpdateComponent;
  let fixture: ComponentFixture<DeporteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deporteService: DeporteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeporteUpdateComponent],
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
      .overrideTemplate(DeporteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeporteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deporteService = TestBed.inject(DeporteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const deporte: IDeporte = { id: 456 };

      activatedRoute.data = of({ deporte });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deporte));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deporte>>();
      const deporte = { id: 123 };
      jest.spyOn(deporteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deporte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deporte }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deporteService.update).toHaveBeenCalledWith(deporte);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deporte>>();
      const deporte = new Deporte();
      jest.spyOn(deporteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deporte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deporte }));
      saveSubject.complete();

      // THEN
      expect(deporteService.create).toHaveBeenCalledWith(deporte);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deporte>>();
      const deporte = { id: 123 };
      jest.spyOn(deporteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deporte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deporteService.update).toHaveBeenCalledWith(deporte);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
