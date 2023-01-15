import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PremioService } from '../service/premio.service';
import { IPremio, Premio } from '../premio.model';

import { PremioUpdateComponent } from './premio-update.component';

describe('Premio Management Update Component', () => {
  let comp: PremioUpdateComponent;
  let fixture: ComponentFixture<PremioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let premioService: PremioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PremioUpdateComponent],
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
      .overrideTemplate(PremioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PremioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    premioService = TestBed.inject(PremioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const premio: IPremio = { id: 456 };

      activatedRoute.data = of({ premio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(premio));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Premio>>();
      const premio = { id: 123 };
      jest.spyOn(premioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ premio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: premio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(premioService.update).toHaveBeenCalledWith(premio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Premio>>();
      const premio = new Premio();
      jest.spyOn(premioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ premio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: premio }));
      saveSubject.complete();

      // THEN
      expect(premioService.create).toHaveBeenCalledWith(premio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Premio>>();
      const premio = { id: 123 };
      jest.spyOn(premioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ premio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(premioService.update).toHaveBeenCalledWith(premio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
