import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OpcionRolService } from '../service/opcion-rol.service';

import { OpcionRolComponent } from './opcion-rol.component';

describe('OpcionRol Management Component', () => {
  let comp: OpcionRolComponent;
  let fixture: ComponentFixture<OpcionRolComponent>;
  let service: OpcionRolService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OpcionRolComponent],
    })
      .overrideTemplate(OpcionRolComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OpcionRolComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OpcionRolService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.opcionRols?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
