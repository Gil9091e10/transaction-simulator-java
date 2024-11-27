import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CardTypeResolve from './route/card-type-routing-resolve.service';

const cardTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/card-type.component').then(m => m.CardTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/card-type-detail.component').then(m => m.CardTypeDetailComponent),
    resolve: {
      cardType: CardTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/card-type-update.component').then(m => m.CardTypeUpdateComponent),
    resolve: {
      cardType: CardTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/card-type-update.component').then(m => m.CardTypeUpdateComponent),
    resolve: {
      cardType: CardTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cardTypeRoute;
