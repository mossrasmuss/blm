import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBusiness, Business } from 'app/shared/model/business.model';
import { BusinessService } from './business.service';
import { BusinessComponent } from './business.component';
import { BusinessDetailComponent } from './business-detail.component';
import { BusinessUpdateComponent } from './business-update.component';

@Injectable({ providedIn: 'root' })
export class BusinessResolve implements Resolve<IBusiness> {
  constructor(private service: BusinessService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusiness> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((business: HttpResponse<Business>) => {
          if (business.body) {
            return of(business.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Business());
  }
}

export const businessRoute: Routes = [
  {
    path: '',
    component: BusinessComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.business.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessDetailComponent,
    resolve: {
      business: BusinessResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.business.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusinessUpdateComponent,
    resolve: {
      business: BusinessResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.business.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessUpdateComponent,
    resolve: {
      business: BusinessResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.business.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
