import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MisionService } from '../service/mision.service';
import { IMision, Mision } from '../mision.model';

import { MisionUpdateComponent } from './mision-update.component';

describe('Mision Management Update Component', () => {
  let comp: MisionUpdateComponent;
  let fixture: ComponentFixture<MisionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let misionService: MisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MisionUpdateComponent],
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
      .overrideTemplate(MisionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MisionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    misionService = TestBed.inject(MisionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const mision: IMision = { id: 456 };

      activatedRoute.data = of({ mision });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(mision));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mision>>();
      const mision = { id: 123 };
      jest.spyOn(misionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mision }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(misionService.update).toHaveBeenCalledWith(mision);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mision>>();
      const mision = new Mision();
      jest.spyOn(misionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mision }));
      saveSubject.complete();

      // THEN
      expect(misionService.create).toHaveBeenCalledWith(mision);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mision>>();
      const mision = { id: 123 };
      jest.spyOn(misionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(misionService.update).toHaveBeenCalledWith(mision);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
