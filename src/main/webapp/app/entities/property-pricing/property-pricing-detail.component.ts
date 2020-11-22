import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPropertyPricing } from 'app/shared/model/property-pricing.model';

@Component({
  selector: 'jhi-property-pricing-detail',
  templateUrl: './property-pricing-detail.component.html',
})
export class PropertyPricingDetailComponent implements OnInit {
  propertyPricing: IPropertyPricing | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propertyPricing }) => (this.propertyPricing = propertyPricing));
  }

  previousState(): void {
    window.history.back();
  }
}
