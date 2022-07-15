import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ApuestaService } from '../service/apuesta.service';

import { ApuestaComponent } from './apuesta.component';

describe('Apuesta Management Component', () => {
  let comp: ApuestaComponent;
  let fixture: ComponentFixture<ApuestaComponent>;
  let service: ApuestaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ApuestaComponent],
    })
      .overrideTemplate(ApuestaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApuestaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ApuestaService);

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
    expect(comp.apuestas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
