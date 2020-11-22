import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPropertyGroup } from 'app/shared/model/property-group.model';
import { PropertyGroupService } from './property-group.service';

@Component({
  templateUrl: './property-group-delete-dialog.component.html',
})
export class PropertyGroupDeleteDialogComponent {
  propertyGroup?: IPropertyGroup;

  constructor(
    protected propertyGroupService: PropertyGroupService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.propertyGroupService.delete(id).subscribe(() => {
      this.eventManager.broadcast('propertyGroupListModification');
      this.activeModal.close();
    });
  }
}
