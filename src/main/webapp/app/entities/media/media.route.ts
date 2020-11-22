import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMedia, Media } from 'app/shared/model/media.model';
import { MediaService } from './media.service';
import { MediaComponent } from './media.component';
import { MediaDetailComponent } from './media-detail.component';
import { MediaUpdateComponent } from './media-update.component';

@Injectable({ providedIn: 'root' })
export class MediaResolve implements Resolve<IMedia> {
  constructor(private service: MediaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMedia> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((media: HttpResponse<Media>) => {
          if (media.body) {
            return of(media.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Media());
  }
}

export const mediaRoute: Routes = [
  {
    path: '',
    component: MediaComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.media.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MediaDetailComponent,
    resolve: {
      media: MediaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.media.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MediaUpdateComponent,
    resolve: {
      media: MediaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.media.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MediaUpdateComponent,
    resolve: {
      media: MediaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.media.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
