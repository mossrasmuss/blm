import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IPropertyGroup } from 'app/shared/model/property-group.model';

type EntityResponseType = HttpResponse<IPropertyGroup>;
type EntityArrayResponseType = HttpResponse<IPropertyGroup[]>;

@Injectable({ providedIn: 'root' })
export class PropertyGroupService {
  public resourceUrl = SERVER_API_URL + 'api/property-groups';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/property-groups';

  constructor(protected http: HttpClient) {}

  create(propertyGroup: IPropertyGroup): Observable<EntityResponseType> {
    return this.http.post<IPropertyGroup>(this.resourceUrl, propertyGroup, { observe: 'response' });
  }

  update(propertyGroup: IPropertyGroup): Observable<EntityResponseType> {
    return this.http.put<IPropertyGroup>(this.resourceUrl, propertyGroup, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPropertyGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPropertyGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPropertyGroup[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
