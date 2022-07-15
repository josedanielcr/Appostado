import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PremioService } from '../service/premio.service';

import { PremioComponent } from './premio.component';

describe('Premio Management Component', () => {
  let comp: PremioComponent;
  let fixture: ComponentFixture<PremioComponent>;
  let service: PremioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PremioComponent],
    })
      .overrideTemplate(PremioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PremioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PremioService);

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
    expect(comp.premios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
