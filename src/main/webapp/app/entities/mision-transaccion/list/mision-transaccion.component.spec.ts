import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MisionTransaccionService } from '../service/mision-transaccion.service';

import { MisionTransaccionComponent } from './mision-transaccion.component';

describe('MisionTransaccion Management Component', () => {
  let comp: MisionTransaccionComponent;
  let fixture: ComponentFixture<MisionTransaccionComponent>;
  let service: MisionTransaccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MisionTransaccionComponent],
    })
      .overrideTemplate(MisionTransaccionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MisionTransaccionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MisionTransaccionService);

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
    expect(comp.misionTransaccions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
