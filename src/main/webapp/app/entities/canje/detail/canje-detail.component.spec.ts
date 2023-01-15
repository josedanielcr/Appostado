import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CanjeDetailComponent } from './canje-detail.component';

describe('Canje Management Detail Component', () => {
  let comp: CanjeDetailComponent;
  let fixture: ComponentFixture<CanjeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CanjeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ canje: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CanjeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CanjeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load canje on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.canje).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
