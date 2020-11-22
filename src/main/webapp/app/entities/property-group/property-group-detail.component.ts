import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPropertyGroup } from 'app/shared/model/property-group.model';

@Component({
  selector: 'jhi-property-group-detail',
  templateUrl: './property-group-detail.component.html',
})
export class PropertyGroupDetailComponent implements OnInit {
  propertyGroup: IPropertyGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ propertyGroup }) => (this.propertyGroup = propertyGroup));
  }

  previousState(): void {
    window.history.back();
  }
}
