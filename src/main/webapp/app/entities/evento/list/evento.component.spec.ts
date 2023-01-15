import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EventoService } from '../service/evento.service';

import { EventoComponent } from './evento.component';

describe('Evento Management Component', () => {
  let comp: EventoComponent;
  let fixture: ComponentFixture<EventoComponent>;
  let service: EventoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EventoComponent],
    })
      .overrideTemplate(EventoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EventoService);

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
    expect(comp.eventos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
