import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MessageTypeIndicatorResolve from './route/message-type-indicator-routing-resolve.service';

const messageTypeIndicatorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/message-type-indicator.component').then(m => m.MessageTypeIndicatorComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/message-type-indicator-detail.component').then(m => m.MessageTypeIndicatorDetailComponent),
    resolve: {
      messageTypeIndicator: MessageTypeIndicatorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/message-type-indicator-update.component').then(m => m.MessageTypeIndicatorUpdateComponent),
    resolve: {
      messageTypeIndicator: MessageTypeIndicatorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/message-type-indicator-update.component').then(m => m.MessageTypeIndicatorUpdateComponent),
    resolve: {
      messageTypeIndicator: MessageTypeIndicatorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default messageTypeIndicatorRoute;
