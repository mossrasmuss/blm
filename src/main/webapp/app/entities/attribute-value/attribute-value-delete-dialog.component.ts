import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAttributeValue } from 'app/shared/model/attribute-value.model';
import { AttributeValueService } from './attribute-value.service';

@Component({
  templateUrl: './attribute-value-delete-dialog.component.html',
})
export class AttributeValueDeleteDialogComponent {
  attributeValue?: IAttributeValue;

  constructor(
    protected attributeValueService: AttributeValueService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attributeValueService.delete(id).subscribe(() => {
      this.eventManager.broadcast('attributeValueListModification');
      this.activeModal.close();
    });
  }
}
