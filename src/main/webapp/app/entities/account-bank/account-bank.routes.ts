import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AccountBankResolve from './route/account-bank-routing-resolve.service';

const accountBankRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/account-bank.component').then(m => m.AccountBankComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/account-bank-detail.component').then(m => m.AccountBankDetailComponent),
    resolve: {
      accountBank: AccountBankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/account-bank-update.component').then(m => m.AccountBankUpdateComponent),
    resolve: {
      accountBank: AccountBankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/account-bank-update.component').then(m => m.AccountBankUpdateComponent),
    resolve: {
      accountBank: AccountBankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accountBankRoute;
