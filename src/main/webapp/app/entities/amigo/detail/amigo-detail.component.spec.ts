import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AmigoDetailComponent } from './amigo-detail.component';

describe('Amigo Management Detail Component', () => {
  let comp: AmigoDetailComponent;
  let fixture: ComponentFixture<AmigoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AmigoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ amigo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AmigoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AmigoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load amigo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.amigo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
