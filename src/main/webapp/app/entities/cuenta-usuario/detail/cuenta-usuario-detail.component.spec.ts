import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CuentaUsuarioDetailComponent } from './cuenta-usuario-detail.component';

describe('CuentaUsuario Management Detail Component', () => {
  let comp: CuentaUsuarioDetailComponent;
  let fixture: ComponentFixture<CuentaUsuarioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CuentaUsuarioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cuentaUsuario: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CuentaUsuarioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CuentaUsuarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cuentaUsuario on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cuentaUsuario).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
