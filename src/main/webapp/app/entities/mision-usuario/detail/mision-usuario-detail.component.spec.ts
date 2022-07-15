import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MisionUsuarioDetailComponent } from './mision-usuario-detail.component';

describe('MisionUsuario Management Detail Component', () => {
  let comp: MisionUsuarioDetailComponent;
  let fixture: ComponentFixture<MisionUsuarioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MisionUsuarioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ misionUsuario: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MisionUsuarioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MisionUsuarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load misionUsuario on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.misionUsuario).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
