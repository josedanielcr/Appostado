import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DivisionCompetidorDetailComponent } from './division-competidor-detail.component';

describe('DivisionCompetidor Management Detail Component', () => {
  let comp: DivisionCompetidorDetailComponent;
  let fixture: ComponentFixture<DivisionCompetidorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DivisionCompetidorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ divisionCompetidor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DivisionCompetidorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DivisionCompetidorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load divisionCompetidor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.divisionCompetidor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
