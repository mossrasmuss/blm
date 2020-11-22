import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISalesProperty } from 'app/shared/model/sales-property.model';
import { SalesPropertyService } from './sales-property.service';

@Component({
  templateUrl: './sales-property-delete-dialog.component.html',
})
export class SalesPropertyDeleteDialogComponent {
  salesProperty?: ISalesProperty;

  constructor(
    protected salesPropertyService: SalesPropertyService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.salesPropertyService.delete(id).subscribe(() => {
      this.eventManager.broadcast('salesPropertyListModification');
      this.activeModal.close();
    });
  }
}
