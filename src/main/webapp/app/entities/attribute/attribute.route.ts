import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAttribute, Attribute } from 'app/shared/model/attribute.model';
import { AttributeService } from './attribute.service';
import { AttributeComponent } from './attribute.component';
import { AttributeDetailComponent } from './attribute-detail.component';
import { AttributeUpdateComponent } from './attribute-update.component';

@Injectable({ providedIn: 'root' })
export class AttributeResolve implements Resolve<IAttribute> {
  constructor(private service: AttributeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttribute> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((attribute: HttpResponse<Attribute>) => {
          if (attribute.body) {
            return of(attribute.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Attribute());
  }
}

export const attributeRoute: Routes = [
  {
    path: '',
    component: AttributeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.attribute.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttributeDetailComponent,
    resolve: {
      attribute: AttributeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.attribute.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttributeUpdateComponent,
    resolve: {
      attribute: AttributeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.attribute.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttributeUpdateComponent,
    resolve: {
      attribute: AttributeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'blmApp.attribute.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
