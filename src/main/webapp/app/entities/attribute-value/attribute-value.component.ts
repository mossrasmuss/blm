import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttributeValue } from 'app/shared/model/attribute-value.model';
import { AttributeValueService } from './attribute-value.service';
import { AttributeValueDeleteDialogComponent } from './attribute-value-delete-dialog.component';

@Component({
  selector: 'jhi-attribute-value',
  templateUrl: './attribute-value.component.html',
})
export class AttributeValueComponent implements OnInit, OnDestroy {
  attributeValues?: IAttributeValue[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected attributeValueService: AttributeValueService,
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
      this.attributeValueService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IAttributeValue[]>) => (this.attributeValues = res.body || []));
      return;
    }

    this.attributeValueService.query().subscribe((res: HttpResponse<IAttributeValue[]>) => (this.attributeValues = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAttributeValues();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAttributeValue): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAttributeValues(): void {
    this.eventSubscriber = this.eventManager.subscribe('attributeValueListModification', () => this.loadAll());
  }

  delete(attributeValue: IAttributeValue): void {
    const modalRef = this.modalService.open(AttributeValueDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.attributeValue = attributeValue;
  }
}
