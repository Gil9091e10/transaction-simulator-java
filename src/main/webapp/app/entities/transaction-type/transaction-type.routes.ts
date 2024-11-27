import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TransactionTypeResolve from './route/transaction-type-routing-resolve.service';

const transactionTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transaction-type.component').then(m => m.TransactionTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transaction-type-detail.component').then(m => m.TransactionTypeDetailComponent),
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transaction-type-update.component').then(m => m.TransactionTypeUpdateComponent),
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transaction-type-update.component').then(m => m.TransactionTypeUpdateComponent),
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transactionTypeRoute;
