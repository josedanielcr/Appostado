import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MisionUsuarioService } from '../service/mision-usuario.service';

import { MisionUsuarioComponent } from './mision-usuario.component';

describe('MisionUsuario Management Component', () => {
  let comp: MisionUsuarioComponent;
  let fixture: ComponentFixture<MisionUsuarioComponent>;
  let service: MisionUsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MisionUsuarioComponent],
    })
      .overrideTemplate(MisionUsuarioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MisionUsuarioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MisionUsuarioService);

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
    expect(comp.misionUsuarios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
