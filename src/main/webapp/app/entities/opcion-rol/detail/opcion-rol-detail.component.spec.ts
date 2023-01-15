import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpcionRolDetailComponent } from './opcion-rol-detail.component';

describe('OpcionRol Management Detail Component', () => {
  let comp: OpcionRolDetailComponent;
  let fixture: ComponentFixture<OpcionRolDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OpcionRolDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ opcionRol: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OpcionRolDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OpcionRolDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load opcionRol on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.opcionRol).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
