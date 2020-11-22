import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BlmSharedModule } from 'app/shared/shared.module';
import { AttributeValueComponent } from './attribute-value.component';
import { AttributeValueDetailComponent } from './attribute-value-detail.component';
import { AttributeValueUpdateComponent } from './attribute-value-update.component';
import { AttributeValueDeleteDialogComponent } from './attribute-value-delete-dialog.component';
import { attributeValueRoute } from './attribute-value.route';

@NgModule({
  imports: [BlmSharedModule, RouterModule.forChild(attributeValueRoute)],
  declarations: [
    AttributeValueComponent,
    AttributeValueDetailComponent,
    AttributeValueUpdateComponent,
    AttributeValueDeleteDialogComponent,
  ],
  entryComponents: [AttributeValueDeleteDialogComponent],
})
export class BlmAttributeValueModule {}
