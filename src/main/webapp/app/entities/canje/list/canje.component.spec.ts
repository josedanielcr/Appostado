import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CanjeService } from '../service/canje.service';

import { CanjeComponent } from './canje.component';

describe('Canje Management Component', () => {
  let comp: CanjeComponent;
  let fixture: ComponentFixture<CanjeComponent>;
  let service: CanjeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CanjeComponent],
    })
      .overrideTemplate(CanjeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CanjeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CanjeService);

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
    expect(comp.canjes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
