import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AmigoService } from '../service/amigo.service';

import { AmigoComponent } from './amigo.component';

describe('Amigo Management Component', () => {
  let comp: AmigoComponent;
  let fixture: ComponentFixture<AmigoComponent>;
  let service: AmigoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AmigoComponent],
    })
      .overrideTemplate(AmigoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AmigoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AmigoService);

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
    expect(comp.amigos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
