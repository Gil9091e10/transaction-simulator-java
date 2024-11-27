import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AcquirerResolve from './route/acquirer-routing-resolve.service';

const acquirerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/acquirer.component').then(m => m.AcquirerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/acquirer-detail.component').then(m => m.AcquirerDetailComponent),
    resolve: {
      acquirer: AcquirerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/acquirer-update.component').then(m => m.AcquirerUpdateComponent),
    resolve: {
      acquirer: AcquirerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/acquirer-update.component').then(m => m.AcquirerUpdateComponent),
    resolve: {
      acquirer: AcquirerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default acquirerRoute;
