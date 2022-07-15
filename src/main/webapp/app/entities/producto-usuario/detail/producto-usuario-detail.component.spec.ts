import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductoUsuarioDetailComponent } from './producto-usuario-detail.component';

describe('ProductoUsuario Management Detail Component', () => {
  let comp: ProductoUsuarioDetailComponent;
  let fixture: ComponentFixture<ProductoUsuarioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductoUsuarioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productoUsuario: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductoUsuarioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductoUsuarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productoUsuario on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productoUsuario).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
