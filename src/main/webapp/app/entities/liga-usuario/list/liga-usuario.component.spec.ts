import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LigaUsuarioService } from '../service/liga-usuario.service';

import { LigaUsuarioComponent } from './liga-usuario.component';

describe('LigaUsuario Management Component', () => {
  let comp: LigaUsuarioComponent;
  let fixture: ComponentFixture<LigaUsuarioComponent>;
  let service: LigaUsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LigaUsuarioComponent],
    })
      .overrideTemplate(LigaUsuarioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LigaUsuarioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LigaUsuarioService);

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
    expect(comp.ligaUsuarios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
