import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BlmTestModule } from '../../../test.module';
import { PropertyPricingComponent } from 'app/entities/property-pricing/property-pricing.component';
import { PropertyPricingService } from 'app/entities/property-pricing/property-pricing.service';
import { PropertyPricing } from 'app/shared/model/property-pricing.model';

describe('Component Tests', () => {
  describe('PropertyPricing Management Component', () => {
    let comp: PropertyPricingComponent;
    let fixture: ComponentFixture<PropertyPricingComponent>;
    let service: PropertyPricingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [PropertyPricingComponent],
      })
        .overrideTemplate(PropertyPricingComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PropertyPricingComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PropertyPricingService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PropertyPricing(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.propertyPricings && comp.propertyPricings[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
