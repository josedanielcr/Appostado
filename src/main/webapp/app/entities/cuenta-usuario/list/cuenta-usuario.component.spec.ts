import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CuentaUsuarioService } from '../service/cuenta-usuario.service';

import { CuentaUsuarioComponent } from './cuenta-usuario.component';

describe('CuentaUsuario Management Component', () => {
  let comp: CuentaUsuarioComponent;
  let fixture: ComponentFixture<CuentaUsuarioComponent>;
  let service: CuentaUsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CuentaUsuarioComponent],
    })
      .overrideTemplate(CuentaUsuarioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CuentaUsuarioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CuentaUsuarioService);

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
    expect(comp.cuentaUsuarios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
