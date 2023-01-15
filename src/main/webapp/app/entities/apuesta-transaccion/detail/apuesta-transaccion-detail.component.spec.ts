import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApuestaTransaccionDetailComponent } from './apuesta-transaccion-detail.component';

describe('ApuestaTransaccion Management Detail Component', () => {
  let comp: ApuestaTransaccionDetailComponent;
  let fixture: ComponentFixture<ApuestaTransaccionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApuestaTransaccionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ apuestaTransaccion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ApuestaTransaccionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ApuestaTransaccionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load apuestaTransaccion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.apuestaTransaccion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
