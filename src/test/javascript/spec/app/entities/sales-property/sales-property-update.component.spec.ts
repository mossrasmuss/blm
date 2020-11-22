import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BlmTestModule } from '../../../test.module';
import { SalesPropertyUpdateComponent } from 'app/entities/sales-property/sales-property-update.component';
import { SalesPropertyService } from 'app/entities/sales-property/sales-property.service';
import { SalesProperty } from 'app/shared/model/sales-property.model';

describe('Component Tests', () => {
  describe('SalesProperty Management Update Component', () => {
    let comp: SalesPropertyUpdateComponent;
    let fixture: ComponentFixture<SalesPropertyUpdateComponent>;
    let service: SalesPropertyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [SalesPropertyUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SalesPropertyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SalesPropertyUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SalesPropertyService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SalesProperty(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new SalesProperty();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
