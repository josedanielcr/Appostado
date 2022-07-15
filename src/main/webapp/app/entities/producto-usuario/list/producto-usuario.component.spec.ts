import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProductoUsuarioService } from '../service/producto-usuario.service';

import { ProductoUsuarioComponent } from './producto-usuario.component';

describe('ProductoUsuario Management Component', () => {
  let comp: ProductoUsuarioComponent;
  let fixture: ComponentFixture<ProductoUsuarioComponent>;
  let service: ProductoUsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProductoUsuarioComponent],
    })
      .overrideTemplate(ProductoUsuarioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductoUsuarioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProductoUsuarioService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.productoUsuarios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
