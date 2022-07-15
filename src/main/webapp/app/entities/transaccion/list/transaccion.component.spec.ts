import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TransaccionService } from '../service/transaccion.service';

import { TransaccionComponent } from './transaccion.component';

describe('Transaccion Management Component', () => {
  let comp: TransaccionComponent;
  let fixture: ComponentFixture<TransaccionComponent>;
  let service: TransaccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TransaccionComponent],
    })
      .overrideTemplate(TransaccionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransaccionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TransaccionService);

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
    expect(comp.transaccions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
