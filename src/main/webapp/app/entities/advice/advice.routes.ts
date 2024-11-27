import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AdviceResolve from './route/advice-routing-resolve.service';

const adviceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/advice.component').then(m => m.AdviceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/advice-detail.component').then(m => m.AdviceDetailComponent),
    resolve: {
      advice: AdviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/advice-update.component').then(m => m.AdviceUpdateComponent),
    resolve: {
      advice: AdviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/advice-update.component').then(m => m.AdviceUpdateComponent),
    resolve: {
      advice: AdviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default adviceRoute;
