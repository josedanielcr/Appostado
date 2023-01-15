import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ApuestaTransaccionService } from '../service/apuesta-transaccion.service';

import { ApuestaTransaccionComponent } from './apuesta-transaccion.component';

describe('ApuestaTransaccion Management Component', () => {
  let comp: ApuestaTransaccionComponent;
  let fixture: ComponentFixture<ApuestaTransaccionComponent>;
  let service: ApuestaTransaccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ApuestaTransaccionComponent],
    })
      .overrideTemplate(ApuestaTransaccionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApuestaTransaccionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ApuestaTransaccionService);

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
    expect(comp.apuestaTransaccions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
