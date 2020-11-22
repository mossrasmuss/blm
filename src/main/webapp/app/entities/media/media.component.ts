import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedia } from 'app/shared/model/media.model';
import { MediaService } from './media.service';
import { MediaDeleteDialogComponent } from './media-delete-dialog.component';

@Component({
  selector: 'jhi-media',
  templateUrl: './media.component.html',
})
export class MediaComponent implements OnInit, OnDestroy {
  media?: IMedia[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected mediaService: MediaService,
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
      this.mediaService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IMedia[]>) => (this.media = res.body || []));
      return;
    }

    this.mediaService.query().subscribe((res: HttpResponse<IMedia[]>) => (this.media = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMedia();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMedia): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMedia(): void {
    this.eventSubscriber = this.eventManager.subscribe('mediaListModification', () => this.loadAll());
  }

  delete(media: IMedia): void {
    const modalRef = this.modalService.open(MediaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.media = media;
  }
}
