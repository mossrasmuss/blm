import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { ISalesProperty } from 'app/shared/model/sales-property.model';

type EntityResponseType = HttpResponse<ISalesProperty>;
type EntityArrayResponseType = HttpResponse<ISalesProperty[]>;

@Injectable({ providedIn: 'root' })
export class SalesPropertyService {
  public resourceUrl = SERVER_API_URL + 'api/sales-properties';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/sales-properties';

  constructor(protected http: HttpClient) {}

  create(salesProperty: ISalesProperty): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salesProperty);
    return this.http
      .post<ISalesProperty>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(salesProperty: ISalesProperty): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salesProperty);
    return this.http
      .put<ISalesProperty>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISalesProperty>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISalesProperty[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISalesProperty[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(salesProperty: ISalesProperty): ISalesProperty {
    const copy: ISalesProperty = Object.assign({}, salesProperty, {
      datePosted: salesProperty.datePosted && salesProperty.datePosted.isValid() ? salesProperty.datePosted.toJSON() : undefined,
      expiryDate: salesProperty.expiryDate && salesProperty.expiryDate.isValid() ? salesProperty.expiryDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datePosted = res.body.datePosted ? moment(res.body.datePosted) : undefined;
      res.body.expiryDate = res.body.expiryDate ? moment(res.body.expiryDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((salesProperty: ISalesProperty) => {
        salesProperty.datePosted = salesProperty.datePosted ? moment(salesProperty.datePosted) : undefined;
        salesProperty.expiryDate = salesProperty.expiryDate ? moment(salesProperty.expiryDate) : undefined;
      });
    }
    return res;
  }
}
