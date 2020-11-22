import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBusiness } from 'app/shared/model/business.model';
import { BusinessService } from './business.service';

@Component({
  templateUrl: './business-delete-dialog.component.html',
})
export class BusinessDeleteDialogComponent {
  business?: IBusiness;

  constructor(protected businessService: BusinessService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.businessService.delete(id).subscribe(() => {
      this.eventManager.broadcast('businessListModification');
      this.activeModal.close();
    });
  }
}
