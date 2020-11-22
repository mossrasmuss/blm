import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IProperty, Property } from 'app/shared/model/property.model';
import { PropertyService } from './property.service';
import { ISalesProperty } from 'app/shared/model/sales-property.model';
import { SalesPropertyService } from 'app/entities/sales-property/sales-property.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';

type SelectableEntity = ISalesProperty | ICategory;

@Component({
  selector: 'jhi-property-update',
  templateUrl: './property-update.component.html',
})
export class PropertyUpdateComponent implements OnInit {
  isSaving = false;
  salesproperties: ISalesProperty[] = [];
  categories: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    area: [],
    description: [],
    salesProperty: [],
    category: [],
  });

  constructor(
    protected propertyService: PropertyService,
    protected salesPropertyService: SalesPropertyService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ property }) => {
      this.updateForm(property);

      this.salesPropertyService
        .query({ filter: 'property-is-null' })
        .pipe(
          map((res: HttpResponse<ISalesProperty[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ISalesProperty[]) => {
          if (!property.salesProperty || !property.salesProperty.id) {
            this.salesproperties = resBody;
          } else {
            this.salesPropertyService
              .find(property.salesProperty.id)
              .pipe(
                map((subRes: HttpResponse<ISalesProperty>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ISalesProperty[]) => (this.salesproperties = concatRes));
          }
        });

      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
    });
  }

  updateForm(property: IProperty): void {
    this.editForm.patchValue({
      id: property.id,
      area: property.area,
      description: property.description,
      salesProperty: property.salesProperty,
      category: property.category,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const property = this.createFromForm();
    if (property.id !== undefined) {
      this.subscribeToSaveResponse(this.propertyService.update(property));
    } else {
      this.subscribeToSaveResponse(this.propertyService.create(property));
    }
  }

  private createFromForm(): IProperty {
    return {
      ...new Property(),
      id: this.editForm.get(['id'])!.value,
      area: this.editForm.get(['area'])!.value,
      description: this.editForm.get(['description'])!.value,
      salesProperty: this.editForm.get(['salesProperty'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProperty>>): void {
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
