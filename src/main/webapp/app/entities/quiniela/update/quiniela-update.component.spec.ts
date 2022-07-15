import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuinielaService } from '../service/quiniela.service';
import { IQuiniela, Quiniela } from '../quiniela.model';

import { QuinielaUpdateComponent } from './quiniela-update.component';

describe('Quiniela Management Update Component', () => {
  let comp: QuinielaUpdateComponent;
  let fixture: ComponentFixture<QuinielaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quinielaService: QuinielaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuinielaUpdateComponent],
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
      .overrideTemplate(QuinielaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuinielaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quinielaService = TestBed.inject(QuinielaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const quiniela: IQuiniela = { id: 456 };

      activatedRoute.data = of({ quiniela });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(quiniela));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Quiniela>>();
      const quiniela = { id: 123 };
      jest.spyOn(quinielaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quiniela });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quiniela }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(quinielaService.update).toHaveBeenCalledWith(quiniela);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Quiniela>>();
      const quiniela = new Quiniela();
      jest.spyOn(quinielaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quiniela });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quiniela }));
      saveSubject.complete();

      // THEN
      expect(quinielaService.create).toHaveBeenCalledWith(quiniela);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Quiniela>>();
      const quiniela = { id: 123 };
      jest.spyOn(quinielaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quiniela });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quinielaService.update).toHaveBeenCalledWith(quiniela);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
