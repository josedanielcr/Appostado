import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { QuinielaService } from '../service/quiniela.service';

import { QuinielaComponent } from './quiniela.component';

describe('Quiniela Management Component', () => {
  let comp: QuinielaComponent;
  let fixture: ComponentFixture<QuinielaComponent>;
  let service: QuinielaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [QuinielaComponent],
    })
      .overrideTemplate(QuinielaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuinielaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(QuinielaService);

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
    expect(comp.quinielas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
