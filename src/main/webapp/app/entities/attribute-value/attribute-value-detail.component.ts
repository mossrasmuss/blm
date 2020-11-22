import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttributeValue } from 'app/shared/model/attribute-value.model';

@Component({
  selector: 'jhi-attribute-value-detail',
  templateUrl: './attribute-value-detail.component.html',
})
export class AttributeValueDetailComponent implements OnInit {
  attributeValue: IAttributeValue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attributeValue }) => (this.attributeValue = attributeValue));
  }

  previousState(): void {
    window.history.back();
  }
}
