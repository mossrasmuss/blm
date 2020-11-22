import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPropertyPricing, PropertyPricing } from 'app/shared/model/property-pricing.model';
import { PropertyPricingService } from './property-pricing.service';
import { PropertyPricingComponent } from './property-pricing.component';
import { PropertyPricingDetailComponent } from './property-pricing-detail.component';
import { PropertyPricingUpdateComponent } from './property-pricing-update.component';

@Injectable({ providedIn: 'root' })
export class PropertyPricingResolve implements Resolve<IPropertyPricing> {
  constructor(private service: PropertyPricingService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPropertyPricing> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((propertyPricing: HttpResponse<PropertyPricing>) => {
          if (propertyPricing.body) {
            return of(propertyPricing.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PropertyPricing());
  }
}

export const propertyPricingRoute: Routes = [
  {
    path: '',
    component: PropertyPricingComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.propertyPricing.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PropertyPricingDetailComponent,
    resolve: {
      propertyPricing: PropertyPricingResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.propertyPricing.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PropertyPricingUpdateComponent,
    resolve: {
      propertyPricing: PropertyPricingResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.propertyPricing.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PropertyPricingUpdateComponent,
    resolve: {
      propertyPricing: PropertyPricingResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.propertyPricing.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
