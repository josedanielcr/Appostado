import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DivisionCompetidorService } from '../service/division-competidor.service';

import { DivisionCompetidorComponent } from './division-competidor.component';

describe('DivisionCompetidor Management Component', () => {
  let comp: DivisionCompetidorComponent;
  let fixture: ComponentFixture<DivisionCompetidorComponent>;
  let service: DivisionCompetidorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DivisionCompetidorComponent],
    })
      .overrideTemplate(DivisionCompetidorComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DivisionCompetidorComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DivisionCompetidorService);

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
    expect(comp.divisionCompetidors?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
