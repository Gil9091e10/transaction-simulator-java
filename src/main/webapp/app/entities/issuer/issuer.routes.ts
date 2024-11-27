import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import IssuerResolve from './route/issuer-routing-resolve.service';

const issuerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/issuer.component').then(m => m.IssuerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/issuer-detail.component').then(m => m.IssuerDetailComponent),
    resolve: {
      issuer: IssuerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/issuer-update.component').then(m => m.IssuerUpdateComponent),
    resolve: {
      issuer: IssuerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/issuer-update.component').then(m => m.IssuerUpdateComponent),
    resolve: {
      issuer: IssuerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default issuerRoute;
