import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPropertyPricing } from 'app/shared/model/property-pricing.model';
import { PropertyPricingService } from './property-pricing.service';
import { PropertyPricingDeleteDialogComponent } from './property-pricing-delete-dialog.component';

@Component({
  selector: 'jhi-property-pricing',
  templateUrl: './property-pricing.component.html',
})
export class PropertyPricingComponent implements OnInit, OnDestroy {
  propertyPricings?: IPropertyPricing[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected propertyPricingService: PropertyPricingService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.propertyPricingService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IPropertyPricing[]>) => (this.propertyPricings = res.body || []));
      return;
    }

    this.propertyPricingService.query().subscribe((res: HttpResponse<IPropertyPricing[]>) => (this.propertyPricings = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPropertyPricings();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPropertyPricing): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPropertyPricings(): void {
    this.eventSubscriber = this.eventManager.subscribe('propertyPricingListModification', () => this.loadAll());
  }

  delete(propertyPricing: IPropertyPricing): void {
    const modalRef = this.modalService.open(PropertyPricingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.propertyPricing = propertyPricing;
  }
}
