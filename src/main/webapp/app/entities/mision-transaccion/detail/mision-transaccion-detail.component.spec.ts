import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MisionTransaccionDetailComponent } from './mision-transaccion-detail.component';

describe('MisionTransaccion Management Detail Component', () => {
  let comp: MisionTransaccionDetailComponent;
  let fixture: ComponentFixture<MisionTransaccionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MisionTransaccionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ misionTransaccion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MisionTransaccionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MisionTransaccionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load misionTransaccion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.misionTransaccion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
