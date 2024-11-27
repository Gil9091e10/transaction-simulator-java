import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DebitCardResolve from './route/debit-card-routing-resolve.service';

const debitCardRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/debit-card.component').then(m => m.DebitCardComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/debit-card-detail.component').then(m => m.DebitCardDetailComponent),
    resolve: {
      debitCard: DebitCardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/debit-card-update.component').then(m => m.DebitCardUpdateComponent),
    resolve: {
      debitCard: DebitCardResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default debitCardRoute;
