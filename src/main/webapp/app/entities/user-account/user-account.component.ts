import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserAccount } from 'app/shared/model/user-account.model';
import { UserAccountService } from './user-account.service';
import { UserAccountDeleteDialogComponent } from './user-account-delete-dialog.component';

@Component({
  selector: 'jhi-user-account',
  templateUrl: './user-account.component.html',
})
export class UserAccountComponent implements OnInit, OnDestroy {
  userAccounts?: IUserAccount[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected userAccountService: UserAccountService,
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
      this.userAccountService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IUserAccount[]>) => (this.userAccounts = res.body || []));
      return;
    }

    this.userAccountService.query().subscribe((res: HttpResponse<IUserAccount[]>) => (this.userAccounts = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInUserAccounts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IUserAccount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInUserAccounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('userAccountListModification', () => this.loadAll());
  }

  delete(userAccount: IUserAccount): void {
    const modalRef = this.modalService.open(UserAccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userAccount = userAccount;
  }
}
