import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuinielaDetailComponent } from './quiniela-detail.component';

describe('Quiniela Management Detail Component', () => {
  let comp: QuinielaDetailComponent;
  let fixture: ComponentFixture<QuinielaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuinielaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ quiniela: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(QuinielaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QuinielaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load quiniela on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.quiniela).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
