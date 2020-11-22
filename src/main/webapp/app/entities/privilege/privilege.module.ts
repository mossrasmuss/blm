import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BlmSharedModule } from 'app/shared/shared.module';
import { PrivilegeComponent } from './privilege.component';
import { PrivilegeDetailComponent } from './privilege-detail.component';
import { PrivilegeUpdateComponent } from './privilege-update.component';
import { PrivilegeDeleteDialogComponent } from './privilege-delete-dialog.component';
import { privilegeRoute } from './privilege.route';

@NgModule({
  imports: [BlmSharedModule, RouterModule.forChild(privilegeRoute)],
  declarations: [PrivilegeComponent, PrivilegeDetailComponent, PrivilegeUpdateComponent, PrivilegeDeleteDialogComponent],
  entryComponents: [PrivilegeDeleteDialogComponent],
})
export class BlmPrivilegeModule {}
