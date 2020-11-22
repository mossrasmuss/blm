import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAttribute } from 'app/shared/model/attribute.model';
import { AttributeService } from './attribute.service';

@Component({
  templateUrl: './attribute-delete-dialog.component.html',
})
export class AttributeDeleteDialogComponent {
  attribute?: IAttribute;

  constructor(protected attributeService: AttributeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attributeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('attributeListModification');
      this.activeModal.close();
    });
  }
}
