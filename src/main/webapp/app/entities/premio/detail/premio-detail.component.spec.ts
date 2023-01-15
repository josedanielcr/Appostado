import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PremioDetailComponent } from './premio-detail.component';

describe('Premio Management Detail Component', () => {
  let comp: PremioDetailComponent;
  let fixture: ComponentFixture<PremioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PremioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ premio: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PremioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PremioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load premio on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.premio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
