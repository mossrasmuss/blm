import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalesProperty } from 'app/shared/model/sales-property.model';

@Component({
  selector: 'jhi-sales-property-detail',
  templateUrl: './sales-property-detail.component.html',
})
export class SalesPropertyDetailComponent implements OnInit {
  salesProperty: ISalesProperty | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salesProperty }) => (this.salesProperty = salesProperty));
  }

  previousState(): void {
    window.history.back();
  }
}
