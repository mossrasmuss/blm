import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BlmSharedModule } from 'app/shared/shared.module';
import { PropertyGroupComponent } from './property-group.component';
import { PropertyGroupDetailComponent } from './property-group-detail.component';
import { PropertyGroupUpdateComponent } from './property-group-update.component';
import { PropertyGroupDeleteDialogComponent } from './property-group-delete-dialog.component';
import { propertyGroupRoute } from './property-group.route';

@NgModule({
  imports: [BlmSharedModule, RouterModule.forChild(propertyGroupRoute)],
  declarations: [PropertyGroupComponent, PropertyGroupDetailComponent, PropertyGroupUpdateComponent, PropertyGroupDeleteDialogComponent],
  entryComponents: [PropertyGroupDeleteDialogComponent],
})
export class BlmPropertyGroupModule {}
