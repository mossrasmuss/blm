import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BlmSharedModule } from 'app/shared/shared.module';
import { BusinessComponent } from './business.component';
import { BusinessDetailComponent } from './business-detail.component';
import { BusinessUpdateComponent } from './business-update.component';
import { BusinessDeleteDialogComponent } from './business-delete-dialog.component';
import { businessRoute } from './business.route';

@NgModule({
  imports: [BlmSharedModule, RouterModule.forChild(businessRoute)],
  declarations: [BusinessComponent, BusinessDetailComponent, BusinessUpdateComponent, BusinessDeleteDialogComponent],
  entryComponents: [BusinessDeleteDialogComponent],
})
export class BlmBusinessModule {}
