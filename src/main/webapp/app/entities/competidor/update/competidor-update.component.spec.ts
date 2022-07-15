import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompetidorService } from '../service/competidor.service';
import { ICompetidor, Competidor } from '../competidor.model';

import { CompetidorUpdateComponent } from './competidor-update.component';

describe('Competidor Management Update Component', () => {
  let comp: CompetidorUpdateComponent;
  let fixture: ComponentFixture<CompetidorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let competidorService: CompetidorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompetidorUpdateComponent],
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
      .overrideTemplate(CompetidorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompetidorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    competidorService = TestBed.inject(CompetidorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const competidor: ICompetidor = { id: 456 };

      activatedRoute.data = of({ competidor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(competidor));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Competidor>>();
      const competidor = { id: 123 };
      jest.spyOn(competidorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competidor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: competidor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(competidorService.update).toHaveBeenCalledWith(competidor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Competidor>>();
      const competidor = new Competidor();
      jest.spyOn(competidorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competidor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: competidor }));
      saveSubject.complete();

      // THEN
      expect(competidorService.create).toHaveBeenCalledWith(competidor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Competidor>>();
      const competidor = { id: 123 };
      jest.spyOn(competidorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competidor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(competidorService.update).toHaveBeenCalledWith(competidor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
