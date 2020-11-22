import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'attribute',
        loadChildren: () => import('./attribute/attribute.module').then(m => m.BlmAttributeModule),
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.BlmCategoryModule),
      },
      {
        path: 'property',
        loadChildren: () => import('./property/property.module').then(m => m.BlmPropertyModule),
      },
      {
        path: 'language',
        loadChildren: () => import('./language/language.module').then(m => m.BlmLanguageModule),
      },
      {
        path: 'country',
        loadChildren: () => import('./country/country.module').then(m => m.BlmCountryModule),
      },
      {
        path: 'city',
        loadChildren: () => import('./city/city.module').then(m => m.BlmCityModule),
      },
      {
        path: 'address',
        loadChildren: () => import('./address/address.module').then(m => m.BlmAddressModule),
      },
      {
        path: 'attribute-value',
        loadChildren: () => import('./attribute-value/attribute-value.module').then(m => m.BlmAttributeValueModule),
      },
      {
        path: 'media',
        loadChildren: () => import('./media/media.module').then(m => m.BlmMediaModule),
      },
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.BlmCustomerModule),
      },
      {
        path: 'business',
        loadChildren: () => import('./business/business.module').then(m => m.BlmBusinessModule),
      },
      {
        path: 'user-account',
        loadChildren: () => import('./user-account/user-account.module').then(m => m.BlmUserAccountModule),
      },
      {
        path: 'privilege',
        loadChildren: () => import('./privilege/privilege.module').then(m => m.BlmPrivilegeModule),
      },
      {
        path: 'sales-property',
        loadChildren: () => import('./sales-property/sales-property.module').then(m => m.BlmSalesPropertyModule),
      },
      {
        path: 'property-pricing',
        loadChildren: () => import('./property-pricing/property-pricing.module').then(m => m.BlmPropertyPricingModule),
      },
      {
        path: 'property-group',
        loadChildren: () => import('./property-group/property-group.module').then(m => m.BlmPropertyGroupModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class BlmEntityModule {}
