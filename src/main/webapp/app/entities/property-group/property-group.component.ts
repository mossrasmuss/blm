import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPropertyGroup } from 'app/shared/model/property-group.model';
import { PropertyGroupService } from './property-group.service';
import { PropertyGroupDeleteDialogComponent } from './property-group-delete-dialog.component';

@Component({
  selector: 'jhi-property-group',
  templateUrl: './property-group.component.html',
})
export class PropertyGroupComponent implements OnInit, OnDestroy {
  propertyGroups?: IPropertyGroup[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected propertyGroupService: PropertyGroupService,
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
      this.propertyGroupService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IPropertyGroup[]>) => (this.propertyGroups = res.body || []));
      return;
    }

    this.propertyGroupService.query().subscribe((res: HttpResponse<IPropertyGroup[]>) => (this.propertyGroups = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPropertyGroups();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPropertyGroup): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPropertyGroups(): void {
    this.eventSubscriber = this.eventManager.subscribe('propertyGroupListModification', () => this.loadAll());
  }

  delete(propertyGroup: IPropertyGroup): void {
    const modalRef = this.modalService.open(PropertyGroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.propertyGroup = propertyGroup;
  }
}
