import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAttributeValue, AttributeValue } from 'app/shared/model/attribute-value.model';
import { AttributeValueService } from './attribute-value.service';
import { AttributeValueComponent } from './attribute-value.component';
import { AttributeValueDetailComponent } from './attribute-value-detail.component';
import { AttributeValueUpdateComponent } from './attribute-value-update.component';

@Injectable({ providedIn: 'root' })
export class AttributeValueResolve implements Resolve<IAttributeValue> {
  constructor(private service: AttributeValueService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttributeValue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((attributeValue: HttpResponse<AttributeValue>) => {
          if (attributeValue.body) {
            return of(attributeValue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AttributeValue());
  }
}

export const attributeValueRoute: Routes = [
  {
    path: '',
    component: AttributeValueComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.attributeValue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttributeValueDetailComponent,
    resolve: {
      attributeValue: AttributeValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.attributeValue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttributeValueUpdateComponent,
    resolve: {
      attributeValue: AttributeValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.attributeValue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttributeValueUpdateComponent,
    resolve: {
      attributeValue: AttributeValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.attributeValue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
