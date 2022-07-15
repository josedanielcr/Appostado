import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApuestaDetailComponent } from './apuesta-detail.component';

describe('Apuesta Management Detail Component', () => {
  let comp: ApuestaDetailComponent;
  let fixture: ComponentFixture<ApuestaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApuestaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ apuesta: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ApuestaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ApuestaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load apuesta on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.apuesta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
