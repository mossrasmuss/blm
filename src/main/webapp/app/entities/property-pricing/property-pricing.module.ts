import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BlmSharedModule } from 'app/shared/shared.module';
import { PropertyPricingComponent } from './property-pricing.component';
import { PropertyPricingDetailComponent } from './property-pricing-detail.component';
import { PropertyPricingUpdateComponent } from './property-pricing-update.component';
import { PropertyPricingDeleteDialogComponent } from './property-pricing-delete-dialog.component';
import { propertyPricingRoute } from './property-pricing.route';

@NgModule({
  imports: [BlmSharedModule, RouterModule.forChild(propertyPricingRoute)],
  declarations: [
    PropertyPricingComponent,
    PropertyPricingDetailComponent,
    PropertyPricingUpdateComponent,
    PropertyPricingDeleteDialogComponent,
  ],
  entryComponents: [PropertyPricingDeleteDialogComponent],
})
export class BlmPropertyPricingModule {}
