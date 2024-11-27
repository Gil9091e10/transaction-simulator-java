import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CreditCardResolve from './route/credit-card-routing-resolve.service';

const creditCardRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/credit-card.component').then(m => m.CreditCardComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/credit-card-detail.component').then(m => m.CreditCardDetailComponent),
    resolve: {
      creditCard: CreditCardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/credit-card-update.component').then(m => m.CreditCardUpdateComponent),
    resolve: {
      creditCard: CreditCardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/credit-card-update.component').then(m => m.CreditCardUpdateComponent),
    resolve: {
      creditCard: CreditCardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default creditCardRoute;
