import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPropertyGroup, PropertyGroup } from 'app/shared/model/property-group.model';
import { PropertyGroupService } from './property-group.service';
import { PropertyGroupComponent } from './property-group.component';
import { PropertyGroupDetailComponent } from './property-group-detail.component';
import { PropertyGroupUpdateComponent } from './property-group-update.component';

@Injectable({ providedIn: 'root' })
export class PropertyGroupResolve implements Resolve<IPropertyGroup> {
  constructor(private service: PropertyGroupService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPropertyGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((propertyGroup: HttpResponse<PropertyGroup>) => {
          if (propertyGroup.body) {
            return of(propertyGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PropertyGroup());
  }
}

export const propertyGroupRoute: Routes = [
  {
    path: '',
    component: PropertyGroupComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.propertyGroup.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PropertyGroupDetailComponent,
    resolve: {
      propertyGroup: PropertyGroupResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.propertyGroup.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PropertyGroupUpdateComponent,
    resolve: {
      propertyGroup: PropertyGroupResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.propertyGroup.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PropertyGroupUpdateComponent,
    resolve: {
      propertyGroup: PropertyGroupResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.propertyGroup.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
