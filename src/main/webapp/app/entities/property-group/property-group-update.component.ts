import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPropertyGroup, PropertyGroup } from 'app/shared/model/property-group.model';
import { PropertyGroupService } from './property-group.service';

@Component({
  selector: 'jhi-property-group-update',
  templateUrl: './property-group-update.component.html',
})
export class PropertyGroupUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    groupName: [],
    description: [],
  });

  constructor(protected propertyGroupService: PropertyGroupService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propertyGroup }) => {
      this.updateForm(propertyGroup);
    });
  }

  updateForm(propertyGroup: IPropertyGroup): void {
    this.editForm.patchValue({
      id: propertyGroup.id,
      groupName: propertyGroup.groupName,
      description: propertyGroup.description,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const propertyGroup = this.createFromForm();
    if (propertyGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.propertyGroupService.update(propertyGroup));
    } else {
      this.subscribeToSaveResponse(this.propertyGroupService.create(propertyGroup));
    }
  }

  private createFromForm(): IPropertyGroup {
    return {
      ...new PropertyGroup(),
      id: this.editForm.get(['id'])!.value,
      groupName: this.editForm.get(['groupName'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPropertyGroup>>): void {
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
