import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MisionDetailComponent } from './mision-detail.component';

describe('Mision Management Detail Component', () => {
  let comp: MisionDetailComponent;
  let fixture: ComponentFixture<MisionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MisionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ mision: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MisionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MisionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mision on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.mision).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
