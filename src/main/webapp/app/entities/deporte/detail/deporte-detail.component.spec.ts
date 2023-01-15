import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeporteDetailComponent } from './deporte-detail.component';

describe('Deporte Management Detail Component', () => {
  let comp: DeporteDetailComponent;
  let fixture: ComponentFixture<DeporteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeporteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deporte: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeporteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeporteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deporte on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deporte).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
