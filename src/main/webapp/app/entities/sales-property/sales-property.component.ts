import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalesProperty } from 'app/shared/model/sales-property.model';
import { SalesPropertyService } from './sales-property.service';
import { SalesPropertyDeleteDialogComponent } from './sales-property-delete-dialog.component';

@Component({
  selector: 'jhi-sales-property',
  templateUrl: './sales-property.component.html',
})
export class SalesPropertyComponent implements OnInit, OnDestroy {
  salesProperties?: ISalesProperty[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected salesPropertyService: SalesPropertyService,
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
      this.salesPropertyService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<ISalesProperty[]>) => (this.salesProperties = res.body || []));
      return;
    }

    this.salesPropertyService.query().subscribe((res: HttpResponse<ISalesProperty[]>) => (this.salesProperties = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSalesProperties();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISalesProperty): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSalesProperties(): void {
    this.eventSubscriber = this.eventManager.subscribe('salesPropertyListModification', () => this.loadAll());
  }

  delete(salesProperty: ISalesProperty): void {
    const modalRef = this.modalService.open(SalesPropertyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.salesProperty = salesProperty;
  }
}
