import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAttribute, Attribute } from 'app/shared/model/attribute.model';
import { AttributeService } from './attribute.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';

@Component({
  selector: 'jhi-attribute-update',
  templateUrl: './attribute-update.component.html',
})
export class AttributeUpdateComponent implements OnInit {
  isSaving = false;
  categories: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    attributeName: [],
    description: [],
    category: [],
  });

  constructor(
    protected attributeService: AttributeService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attribute }) => {
      this.updateForm(attribute);

      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
    });
  }

  updateForm(attribute: IAttribute): void {
    this.editForm.patchValue({
      id: attribute.id,
      attributeName: attribute.attributeName,
      description: attribute.description,
      category: attribute.category,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attribute = this.createFromForm();
    if (attribute.id !== undefined) {
      this.subscribeToSaveResponse(this.attributeService.update(attribute));
    } else {
      this.subscribeToSaveResponse(this.attributeService.create(attribute));
    }
  }

  private createFromForm(): IAttribute {
    return {
      ...new Attribute(),
      id: this.editForm.get(['id'])!.value,
      attributeName: this.editForm.get(['attributeName'])!.value,
      description: this.editForm.get(['description'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttribute>>): void {
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

  trackById(index: number, item: ICategory): any {
    return item.id;
  }
}
