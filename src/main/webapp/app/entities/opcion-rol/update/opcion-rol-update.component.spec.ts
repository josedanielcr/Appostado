import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OpcionRolService } from '../service/opcion-rol.service';
import { IOpcionRol, OpcionRol } from '../opcion-rol.model';

import { OpcionRolUpdateComponent } from './opcion-rol-update.component';

describe('OpcionRol Management Update Component', () => {
  let comp: OpcionRolUpdateComponent;
  let fixture: ComponentFixture<OpcionRolUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let opcionRolService: OpcionRolService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OpcionRolUpdateComponent],
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
      .overrideTemplate(OpcionRolUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OpcionRolUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    opcionRolService = TestBed.inject(OpcionRolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const opcionRol: IOpcionRol = { id: 456 };

      activatedRoute.data = of({ opcionRol });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(opcionRol));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OpcionRol>>();
      const opcionRol = { id: 123 };
      jest.spyOn(opcionRolService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opcionRol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: opcionRol }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(opcionRolService.update).toHaveBeenCalledWith(opcionRol);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OpcionRol>>();
      const opcionRol = new OpcionRol();
      jest.spyOn(opcionRolService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opcionRol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: opcionRol }));
      saveSubject.complete();

      // THEN
      expect(opcionRolService.create).toHaveBeenCalledWith(opcionRol);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OpcionRol>>();
      const opcionRol = { id: 123 };
      jest.spyOn(opcionRolService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opcionRol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(opcionRolService.update).toHaveBeenCalledWith(opcionRol);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
