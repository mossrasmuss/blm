import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPropertyPricing, PropertyPricing } from 'app/shared/model/property-pricing.model';
import { PropertyPricingService } from './property-pricing.service';

@Component({
  selector: 'jhi-property-pricing-update',
  templateUrl: './property-pricing-update.component.html',
})
export class PropertyPricingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    defaultPrice: [],
    currentPrice: [],
    description: [],
  });

  constructor(
    protected propertyPricingService: PropertyPricingService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propertyPricing }) => {
      this.updateForm(propertyPricing);
    });
  }

  updateForm(propertyPricing: IPropertyPricing): void {
    this.editForm.patchValue({
      id: propertyPricing.id,
      defaultPrice: propertyPricing.defaultPrice,
      currentPrice: propertyPricing.currentPrice,
      description: propertyPricing.description,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const propertyPricing = this.createFromForm();
    if (propertyPricing.id !== undefined) {
      this.subscribeToSaveResponse(this.propertyPricingService.update(propertyPricing));
    } else {
      this.subscribeToSaveResponse(this.propertyPricingService.create(propertyPricing));
    }
  }

  private createFromForm(): IPropertyPricing {
    return {
      ...new PropertyPricing(),
      id: this.editForm.get(['id'])!.value,
      defaultPrice: this.editForm.get(['defaultPrice'])!.value,
      currentPrice: this.editForm.get(['currentPrice'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPropertyPricing>>): void {
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
