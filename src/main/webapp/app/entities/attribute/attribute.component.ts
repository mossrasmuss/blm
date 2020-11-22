import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttribute } from 'app/shared/model/attribute.model';
import { AttributeService } from './attribute.service';
import { AttributeDeleteDialogComponent } from './attribute-delete-dialog.component';

@Component({
  selector: 'jhi-attribute',
  templateUrl: './attribute.component.html',
})
export class AttributeComponent implements OnInit, OnDestroy {
  attributes?: IAttribute[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected attributeService: AttributeService,
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
      this.attributeService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IAttribute[]>) => (this.attributes = res.body || []));
      return;
    }

    this.attributeService.query().subscribe((res: HttpResponse<IAttribute[]>) => (this.attributes = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAttributes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAttribute): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAttributes(): void {
    this.eventSubscriber = this.eventManager.subscribe('attributeListModification', () => this.loadAll());
  }

  delete(attribute: IAttribute): void {
    const modalRef = this.modalService.open(AttributeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.attribute = attribute;
  }
}
