import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LigaUsuarioDetailComponent } from './liga-usuario-detail.component';

describe('LigaUsuario Management Detail Component', () => {
  let comp: LigaUsuarioDetailComponent;
  let fixture: ComponentFixture<LigaUsuarioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LigaUsuarioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ligaUsuario: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LigaUsuarioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LigaUsuarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ligaUsuario on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ligaUsuario).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
