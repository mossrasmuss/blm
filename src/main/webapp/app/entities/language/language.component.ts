import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILanguage } from 'app/shared/model/language.model';
import { LanguageService } from './language.service';
import { LanguageDeleteDialogComponent } from './language-delete-dialog.component';

@Component({
  selector: 'jhi-language',
  templateUrl: './language.component.html',
})
export class LanguageComponent implements OnInit, OnDestroy {
  languages?: ILanguage[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected languageService: LanguageService,
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
      this.languageService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<ILanguage[]>) => (this.languages = res.body || []));
      return;
    }

    this.languageService.query().subscribe((res: HttpResponse<ILanguage[]>) => (this.languages = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLanguages();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILanguage): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLanguages(): void {
    this.eventSubscriber = this.eventManager.subscribe('languageListModification', () => this.loadAll());
  }

  delete(language: ILanguage): void {
    const modalRef = this.modalService.open(LanguageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.language = language;
  }
}
