import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BlmTestModule } from '../../../test.module';
import { PropertyPricingUpdateComponent } from 'app/entities/property-pricing/property-pricing-update.component';
import { PropertyPricingService } from 'app/entities/property-pricing/property-pricing.service';
import { PropertyPricing } from 'app/shared/model/property-pricing.model';

describe('Component Tests', () => {
  describe('PropertyPricing Management Update Component', () => {
    let comp: PropertyPricingUpdateComponent;
    let fixture: ComponentFixture<PropertyPricingUpdateComponent>;
    let service: PropertyPricingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [PropertyPricingUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PropertyPricingUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PropertyPricingUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PropertyPricingService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PropertyPricing(123);
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
        const entity = new PropertyPricing();
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
