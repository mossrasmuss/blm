import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPropertyPricing } from 'app/shared/model/property-pricing.model';
import { PropertyPricingService } from './property-pricing.service';

@Component({
  templateUrl: './property-pricing-delete-dialog.component.html',
})
export class PropertyPricingDeleteDialogComponent {
  propertyPricing?: IPropertyPricing;

  constructor(
    protected propertyPricingService: PropertyPricingService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.propertyPricingService.delete(id).subscribe(() => {
      this.eventManager.broadcast('propertyPricingListModification');
      this.activeModal.close();
    });
  }
}
