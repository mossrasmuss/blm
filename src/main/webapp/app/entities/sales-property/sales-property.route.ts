import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISalesProperty, SalesProperty } from 'app/shared/model/sales-property.model';
import { SalesPropertyService } from './sales-property.service';
import { SalesPropertyComponent } from './sales-property.component';
import { SalesPropertyDetailComponent } from './sales-property-detail.component';
import { SalesPropertyUpdateComponent } from './sales-property-update.component';

@Injectable({ providedIn: 'root' })
export class SalesPropertyResolve implements Resolve<ISalesProperty> {
  constructor(private service: SalesPropertyService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalesProperty> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((salesProperty: HttpResponse<SalesProperty>) => {
          if (salesProperty.body) {
            return of(salesProperty.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SalesProperty());
  }
}

export const salesPropertyRoute: Routes = [
  {
    path: '',
    component: SalesPropertyComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.salesProperty.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SalesPropertyDetailComponent,
    resolve: {
      salesProperty: SalesPropertyResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.salesProperty.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SalesPropertyUpdateComponent,
    resolve: {
      salesProperty: SalesPropertyResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.salesProperty.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SalesPropertyUpdateComponent,
    resolve: {
      salesProperty: SalesPropertyResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.salesProperty.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
