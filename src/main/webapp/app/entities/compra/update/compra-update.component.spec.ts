import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompraService } from '../service/compra.service';
import { ICompra, Compra } from '../compra.model';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';
import { TransaccionService } from 'app/entities/transaccion/service/transaccion.service';
import { IProducto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';

import { CompraUpdateComponent } from './compra-update.component';

describe('Compra Management Update Component', () => {
  let comp: CompraUpdateComponent;
  let fixture: ComponentFixture<CompraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compraService: CompraService;
  let transaccionService: TransaccionService;
  let productoService: ProductoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompraUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CompraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compraService = TestBed.inject(CompraService);
    transaccionService = TestBed.inject(TransaccionService);
    productoService = TestBed.inject(ProductoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call transaccion query and add missing value', () => {
      const compra: ICompra = { id: 456 };
      const transaccion: ITransaccion = { id: 87768 };
      compra.transaccion = transaccion;

      const transaccionCollection: ITransaccion[] = [{ id: 46592 }];
      jest.spyOn(transaccionService, 'query').mockReturnValue(of(new HttpResponse({ body: transaccionCollection })));
      const expectedCollection: ITransaccion[] = [transaccion, ...transaccionCollection];
      jest.spyOn(transaccionService, 'addTransaccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      expect(transaccionService.query).toHaveBeenCalled();
      expect(transaccionService.addTransaccionToCollectionIfMissing).toHaveBeenCalledWith(transaccionCollection, transaccion);
      expect(comp.transaccionsCollection).toEqual(expectedCollection);
    });

    it('Should call Producto query and add missing value', () => {
      const compra: ICompra = { id: 456 };
      const producto: IProducto = { id: 40363 };
      compra.producto = producto;

      const productoCollection: IProducto[] = [{ id: 82808 }];
      jest.spyOn(productoService, 'query').mockReturnValue(of(new HttpResponse({ body: productoCollection })));
      const additionalProductos = [producto];
      const expectedCollection: IProducto[] = [...additionalProductos, ...productoCollection];
      jest.spyOn(productoService, 'addProductoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      expect(productoService.query).toHaveBeenCalled();
      expect(productoService.addProductoToCollectionIfMissing).toHaveBeenCalledWith(productoCollection, ...additionalProductos);
      expect(comp.productosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const compra: ICompra = { id: 456 };
      const transaccion: ITransaccion = { id: 58222 };
      compra.transaccion = transaccion;
      const producto: IProducto = { id: 86585 };
      compra.producto = producto;

      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(compra));
      expect(comp.transaccionsCollection).toContain(transaccion);
      expect(comp.productosSharedCollection).toContain(producto);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compra>>();
      const compra = { id: 123 };
      jest.spyOn(compraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compra }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(compraService.update).toHaveBeenCalledWith(compra);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compra>>();
      const compra = new Compra();
      jest.spyOn(compraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compra }));
      saveSubject.complete();

      // THEN
      expect(compraService.create).toHaveBeenCalledWith(compra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compra>>();
      const compra = { id: 123 };
      jest.spyOn(compraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compraService.update).toHaveBeenCalledWith(compra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTransaccionById', () => {
      it('Should return tracked Transaccion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransaccionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProductoById', () => {
      it('Should return tracked Producto primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
