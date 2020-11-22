import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IPropertyPricing } from 'app/shared/model/property-pricing.model';

type EntityResponseType = HttpResponse<IPropertyPricing>;
type EntityArrayResponseType = HttpResponse<IPropertyPricing[]>;

@Injectable({ providedIn: 'root' })
export class PropertyPricingService {
  public resourceUrl = SERVER_API_URL + 'api/property-pricings';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/property-pricings';

  constructor(protected http: HttpClient) {}

  create(propertyPricing: IPropertyPricing): Observable<EntityResponseType> {
    return this.http.post<IPropertyPricing>(this.resourceUrl, propertyPricing, { observe: 'response' });
  }

  update(propertyPricing: IPropertyPricing): Observable<EntityResponseType> {
    return this.http.put<IPropertyPricing>(this.resourceUrl, propertyPricing, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPropertyPricing>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPropertyPricing[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPropertyPricing[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
