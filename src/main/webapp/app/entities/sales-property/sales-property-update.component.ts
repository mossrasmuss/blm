import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ISalesProperty, SalesProperty } from 'app/shared/model/sales-property.model';
import { SalesPropertyService } from './sales-property.service';
import { IUserAccount } from 'app/shared/model/user-account.model';
import { UserAccountService } from 'app/entities/user-account/user-account.service';

@Component({
  selector: 'jhi-sales-property-update',
  templateUrl: './sales-property-update.component.html',
})
export class SalesPropertyUpdateComponent implements OnInit {
  isSaving = false;
  useraccounts: IUserAccount[] = [];

  editForm = this.fb.group({
    id: [],
    datePosted: [],
    expiryDate: [],
    type: [],
    status: [],
    defaultPrice: [],
    account: [],
  });

  constructor(
    protected salesPropertyService: SalesPropertyService,
    protected userAccountService: UserAccountService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salesProperty }) => {
      if (!salesProperty.id) {
        const today = moment().startOf('day');
        salesProperty.datePosted = today;
        salesProperty.expiryDate = today;
      }

      this.updateForm(salesProperty);

      this.userAccountService.query().subscribe((res: HttpResponse<IUserAccount[]>) => (this.useraccounts = res.body || []));
    });
  }

  updateForm(salesProperty: ISalesProperty): void {
    this.editForm.patchValue({
      id: salesProperty.id,
      datePosted: salesProperty.datePosted ? salesProperty.datePosted.format(DATE_TIME_FORMAT) : null,
      expiryDate: salesProperty.expiryDate ? salesProperty.expiryDate.format(DATE_TIME_FORMAT) : null,
      type: salesProperty.type,
      status: salesProperty.status,
      defaultPrice: salesProperty.defaultPrice,
      account: salesProperty.account,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salesProperty = this.createFromForm();
    if (salesProperty.id !== undefined) {
      this.subscribeToSaveResponse(this.salesPropertyService.update(salesProperty));
    } else {
      this.subscribeToSaveResponse(this.salesPropertyService.create(salesProperty));
    }
  }

  private createFromForm(): ISalesProperty {
    return {
      ...new SalesProperty(),
      id: this.editForm.get(['id'])!.value,
      datePosted: this.editForm.get(['datePosted'])!.value ? moment(this.editForm.get(['datePosted'])!.value, DATE_TIME_FORMAT) : undefined,
      expiryDate: this.editForm.get(['expiryDate'])!.value ? moment(this.editForm.get(['expiryDate'])!.value, DATE_TIME_FORMAT) : undefined,
      type: this.editForm.get(['type'])!.value,
      status: this.editForm.get(['status'])!.value,
      defaultPrice: this.editForm.get(['defaultPrice'])!.value,
      account: this.editForm.get(['account'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalesProperty>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUserAccount): any {
    return item.id;
  }
}
