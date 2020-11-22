import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAttributeValue, AttributeValue } from 'app/shared/model/attribute-value.model';
import { AttributeValueService } from './attribute-value.service';
import { IAttribute } from 'app/shared/model/attribute.model';
import { AttributeService } from 'app/entities/attribute/attribute.service';
import { IProperty } from 'app/shared/model/property.model';
import { PropertyService } from 'app/entities/property/property.service';

type SelectableEntity = IAttribute | IProperty;

@Component({
  selector: 'jhi-attribute-value-update',
  templateUrl: './attribute-value-update.component.html',
})
export class AttributeValueUpdateComponent implements OnInit {
  isSaving = false;
  attributes: IAttribute[] = [];
  properties: IProperty[] = [];

  editForm = this.fb.group({
    id: [],
    value: [],
    description: [],
    attribute: [],
    property: [],
  });

  constructor(
    protected attributeValueService: AttributeValueService,
    protected attributeService: AttributeService,
    protected propertyService: PropertyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attributeValue }) => {
      this.updateForm(attributeValue);

      this.attributeService.query().subscribe((res: HttpResponse<IAttribute[]>) => (this.attributes = res.body || []));

      this.propertyService.query().subscribe((res: HttpResponse<IProperty[]>) => (this.properties = res.body || []));
    });
  }

  updateForm(attributeValue: IAttributeValue): void {
    this.editForm.patchValue({
      id: attributeValue.id,
      value: attributeValue.value,
      description: attributeValue.description,
      attribute: attributeValue.attribute,
      property: attributeValue.property,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attributeValue = this.createFromForm();
    if (attributeValue.id !== undefined) {
      this.subscribeToSaveResponse(this.attributeValueService.update(attributeValue));
    } else {
      this.subscribeToSaveResponse(this.attributeValueService.create(attributeValue));
    }
  }

  private createFromForm(): IAttributeValue {
    return {
      ...new AttributeValue(),
      id: this.editForm.get(['id'])!.value,
      value: this.editForm.get(['value'])!.value,
      description: this.editForm.get(['description'])!.value,
      attribute: this.editForm.get(['attribute'])!.value,
      property: this.editForm.get(['property'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttributeValue>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
