import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IBusiness, Business } from 'app/shared/model/business.model';
import { BusinessService } from './business.service';

@Component({
  selector: 'jhi-business-update',
  templateUrl: './business-update.component.html',
})
export class BusinessUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    businessName: [],
    year: [],
  });

  constructor(protected businessService: BusinessService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ business }) => {
      if (!business.id) {
        const today = moment().startOf('day');
        business.year = today;
      }

      this.updateForm(business);
    });
  }

  updateForm(business: IBusiness): void {
    this.editForm.patchValue({
      id: business.id,
      businessName: business.businessName,
      year: business.year ? business.year.format(DATE_TIME_FORMAT) : null,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const business = this.createFromForm();
    if (business.id !== undefined) {
      this.subscribeToSaveResponse(this.businessService.update(business));
    } else {
      this.subscribeToSaveResponse(this.businessService.create(business));
    }
  }

  private createFromForm(): IBusiness {
    return {
      ...new Business(),
      id: this.editForm.get(['id'])!.value,
      businessName: this.editForm.get(['businessName'])!.value,
      year: this.editForm.get(['year'])!.value ? moment(this.editForm.get(['year'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusiness>>): void {
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
}
