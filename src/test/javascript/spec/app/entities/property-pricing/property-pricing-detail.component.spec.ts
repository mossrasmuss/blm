import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlmTestModule } from '../../../test.module';
import { PropertyPricingDetailComponent } from 'app/entities/property-pricing/property-pricing-detail.component';
import { PropertyPricing } from 'app/shared/model/property-pricing.model';

describe('Component Tests', () => {
  describe('PropertyPricing Management Detail Component', () => {
    let comp: PropertyPricingDetailComponent;
    let fixture: ComponentFixture<PropertyPricingDetailComponent>;
    const route = ({ data: of({ propertyPricing: new PropertyPricing(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [PropertyPricingDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PropertyPricingDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PropertyPricingDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load propertyPricing on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.propertyPricing).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
