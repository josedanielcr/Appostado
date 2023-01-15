import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LigaDetailComponent } from './liga-detail.component';

describe('Liga Management Detail Component', () => {
  let comp: LigaDetailComponent;
  let fixture: ComponentFixture<LigaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LigaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ liga: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LigaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LigaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load liga on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.liga).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
