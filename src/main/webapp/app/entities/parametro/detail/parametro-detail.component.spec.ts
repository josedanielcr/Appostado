import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParametroDetailComponent } from './parametro-detail.component';

describe('Parametro Management Detail Component', () => {
  let comp: ParametroDetailComponent;
  let fixture: ComponentFixture<ParametroDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ParametroDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ parametro: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ParametroDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ParametroDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load parametro on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.parametro).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
