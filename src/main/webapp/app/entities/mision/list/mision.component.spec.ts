import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MisionService } from '../service/mision.service';

import { MisionComponent } from './mision.component';

describe('Mision Management Component', () => {
  let comp: MisionComponent;
  let fixture: ComponentFixture<MisionComponent>;
  let service: MisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MisionComponent],
    })
      .overrideTemplate(MisionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MisionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MisionService);

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
    expect(comp.misions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
