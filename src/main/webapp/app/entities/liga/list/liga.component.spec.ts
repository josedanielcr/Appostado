import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LigaService } from '../service/liga.service';

import { LigaComponent } from './liga.component';

describe('Liga Management Component', () => {
  let comp: LigaComponent;
  let fixture: ComponentFixture<LigaComponent>;
  let service: LigaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LigaComponent],
    })
      .overrideTemplate(LigaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LigaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LigaService);

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
    expect(comp.ligas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
