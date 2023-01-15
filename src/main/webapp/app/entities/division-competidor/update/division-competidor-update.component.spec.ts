import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DivisionCompetidorService } from '../service/division-competidor.service';
import { IDivisionCompetidor, DivisionCompetidor } from '../division-competidor.model';
import { IDivision } from 'app/entities/division/division.model';
import { DivisionService } from 'app/entities/division/service/division.service';
import { ICompetidor } from 'app/entities/competidor/competidor.model';
import { CompetidorService } from 'app/entities/competidor/service/competidor.service';

import { DivisionCompetidorUpdateComponent } from './division-competidor-update.component';

describe('DivisionCompetidor Management Update Component', () => {
  let comp: DivisionCompetidorUpdateComponent;
  let fixture: ComponentFixture<DivisionCompetidorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let divisionCompetidorService: DivisionCompetidorService;
  let divisionService: DivisionService;
  let competidorService: CompetidorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DivisionCompetidorUpdateComponent],
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
      .overrideTemplate(DivisionCompetidorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DivisionCompetidorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    divisionCompetidorService = TestBed.inject(DivisionCompetidorService);
    divisionService = TestBed.inject(DivisionService);
    competidorService = TestBed.inject(CompetidorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Division query and add missing value', () => {
      const divisionCompetidor: IDivisionCompetidor = { id: 456 };
      const division: IDivision = { id: 30315 };
      divisionCompetidor.division = division;

      const divisionCollection: IDivision[] = [{ id: 55908 }];
      jest.spyOn(divisionService, 'query').mockReturnValue(of(new HttpResponse({ body: divisionCollection })));
      const additionalDivisions = [division];
      const expectedCollection: IDivision[] = [...additionalDivisions, ...divisionCollection];
      jest.spyOn(divisionService, 'addDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ divisionCompetidor });
      comp.ngOnInit();

      expect(divisionService.query).toHaveBeenCalled();
      expect(divisionService.addDivisionToCollectionIfMissing).toHaveBeenCalledWith(divisionCollection, ...additionalDivisions);
      expect(comp.divisionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Competidor query and add missing value', () => {
      const divisionCompetidor: IDivisionCompetidor = { id: 456 };
      const competidor: ICompetidor = { id: 69404 };
      divisionCompetidor.competidor = competidor;

      const competidorCollection: ICompetidor[] = [{ id: 16505 }];
      jest.spyOn(competidorService, 'query').mockReturnValue(of(new HttpResponse({ body: competidorCollection })));
      const additionalCompetidors = [competidor];
      const expectedCollection: ICompetidor[] = [...additionalCompetidors, ...competidorCollection];
      jest.spyOn(competidorService, 'addCompetidorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ divisionCompetidor });
      comp.ngOnInit();

      expect(competidorService.query).toHaveBeenCalled();
      expect(competidorService.addCompetidorToCollectionIfMissing).toHaveBeenCalledWith(competidorCollection, ...additionalCompetidors);
      expect(comp.competidorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const divisionCompetidor: IDivisionCompetidor = { id: 456 };
      const division: IDivision = { id: 81084 };
      divisionCompetidor.division = division;
      const competidor: ICompetidor = { id: 2790 };
      divisionCompetidor.competidor = competidor;

      activatedRoute.data = of({ divisionCompetidor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(divisionCompetidor));
      expect(comp.divisionsSharedCollection).toContain(division);
      expect(comp.competidorsSharedCollection).toContain(competidor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DivisionCompetidor>>();
      const divisionCompetidor = { id: 123 };
      jest.spyOn(divisionCompetidorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ divisionCompetidor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: divisionCompetidor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(divisionCompetidorService.update).toHaveBeenCalledWith(divisionCompetidor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DivisionCompetidor>>();
      const divisionCompetidor = new DivisionCompetidor();
      jest.spyOn(divisionCompetidorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ divisionCompetidor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: divisionCompetidor }));
      saveSubject.complete();

      // THEN
      expect(divisionCompetidorService.create).toHaveBeenCalledWith(divisionCompetidor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DivisionCompetidor>>();
      const divisionCompetidor = { id: 123 };
      jest.spyOn(divisionCompetidorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ divisionCompetidor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(divisionCompetidorService.update).toHaveBeenCalledWith(divisionCompetidor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDivisionById', () => {
      it('Should return tracked Division primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDivisionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCompetidorById', () => {
      it('Should return tracked Competidor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompetidorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
