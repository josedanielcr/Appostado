import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AmigoService } from '../service/amigo.service';
import { IAmigo, Amigo } from '../amigo.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { AmigoUpdateComponent } from './amigo-update.component';

describe('Amigo Management Update Component', () => {
  let comp: AmigoUpdateComponent;
  let fixture: ComponentFixture<AmigoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let amigoService: AmigoService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AmigoUpdateComponent],
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
      .overrideTemplate(AmigoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AmigoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    amigoService = TestBed.inject(AmigoService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const amigo: IAmigo = { id: 456 };
      const usuario: IUsuario = { id: 89495 };
      amigo.usuario = usuario;
      const amigo: IUsuario = { id: 94301 };
      amigo.amigo = amigo;

      const usuarioCollection: IUsuario[] = [{ id: 59467 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario, amigo];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amigo });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const amigo: IAmigo = { id: 456 };
      const usuario: IUsuario = { id: 5903 };
      amigo.usuario = usuario;
      const amigo: IUsuario = { id: 40057 };
      amigo.amigo = amigo;

      activatedRoute.data = of({ amigo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(amigo));
      expect(comp.usuariosSharedCollection).toContain(usuario);
      expect(comp.usuariosSharedCollection).toContain(amigo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Amigo>>();
      const amigo = { id: 123 };
      jest.spyOn(amigoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amigo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amigo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(amigoService.update).toHaveBeenCalledWith(amigo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Amigo>>();
      const amigo = new Amigo();
      jest.spyOn(amigoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amigo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amigo }));
      saveSubject.complete();

      // THEN
      expect(amigoService.create).toHaveBeenCalledWith(amigo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Amigo>>();
      const amigo = { id: 123 };
      jest.spyOn(amigoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amigo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(amigoService.update).toHaveBeenCalledWith(amigo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
