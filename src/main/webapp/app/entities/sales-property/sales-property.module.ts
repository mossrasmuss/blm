import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BlmSharedModule } from 'app/shared/shared.module';
import { SalesPropertyComponent } from './sales-property.component';
import { SalesPropertyDetailComponent } from './sales-property-detail.component';
import { SalesPropertyUpdateComponent } from './sales-property-update.component';
import { SalesPropertyDeleteDialogComponent } from './sales-property-delete-dialog.component';
import { salesPropertyRoute } from './sales-property.route';

@NgModule({
  imports: [BlmSharedModule, RouterModule.forChild(salesPropertyRoute)],
  declarations: [SalesPropertyComponent, SalesPropertyDetailComponent, SalesPropertyUpdateComponent, SalesPropertyDeleteDialogComponent],
  entryComponents: [SalesPropertyDeleteDialogComponent],
})
export class BlmSalesPropertyModule {}
