import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ParametroService } from '../service/parametro.service';

import { ParametroComponent } from './parametro.component';

describe('Parametro Management Component', () => {
  let comp: ParametroComponent;
  let fixture: ComponentFixture<ParametroComponent>;
  let service: ParametroService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ParametroComponent],
    })
      .overrideTemplate(ParametroComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParametroComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ParametroService);

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
    expect(comp.parametros?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
