import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IMedia } from 'app/shared/model/media.model';

type EntityResponseType = HttpResponse<IMedia>;
type EntityArrayResponseType = HttpResponse<IMedia[]>;

@Injectable({ providedIn: 'root' })
export class MediaService {
  public resourceUrl = SERVER_API_URL + 'api/media';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/media';

  constructor(protected http: HttpClient) {}

  create(media: IMedia): Observable<EntityResponseType> {
    return this.http.post<IMedia>(this.resourceUrl, media, { observe: 'response' });
  }

  update(media: IMedia): Observable<EntityResponseType> {
    return this.http.put<IMedia>(this.resourceUrl, media, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedia[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
