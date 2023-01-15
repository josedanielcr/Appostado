import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LigaService } from '../service/liga.service';
import { ILiga, Liga } from '../liga.model';

import { LigaUpdateComponent } from './liga-update.component';

describe('Liga Management Update Component', () => {
  let comp: LigaUpdateComponent;
  let fixture: ComponentFixture<LigaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ligaService: LigaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LigaUpdateComponent],
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
      .overrideTemplate(LigaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LigaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ligaService = TestBed.inject(LigaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const liga: ILiga = { id: 456 };

      activatedRoute.data = of({ liga });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(liga));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Liga>>();
      const liga = { id: 123 };
      jest.spyOn(ligaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ liga });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: liga }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ligaService.update).toHaveBeenCalledWith(liga);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Liga>>();
      const liga = new Liga();
      jest.spyOn(ligaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ liga });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: liga }));
      saveSubject.complete();

      // THEN
      expect(ligaService.create).toHaveBeenCalledWith(liga);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Liga>>();
      const liga = { id: 123 };
      jest.spyOn(ligaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ liga });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ligaService.update).toHaveBeenCalledWith(liga);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
