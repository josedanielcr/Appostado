import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompetidorDetailComponent } from './competidor-detail.component';

describe('Competidor Management Detail Component', () => {
  let comp: CompetidorDetailComponent;
  let fixture: ComponentFixture<CompetidorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompetidorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ competidor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CompetidorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CompetidorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load competidor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.competidor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
