import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MessageIsoConfigResolve from './route/message-iso-config-routing-resolve.service';

const messageIsoConfigRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/message-iso-config.component').then(m => m.MessageIsoConfigComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/message-iso-config-detail.component').then(m => m.MessageIsoConfigDetailComponent),
    resolve: {
      messageIsoConfig: MessageIsoConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/message-iso-config-update.component').then(m => m.MessageIsoConfigUpdateComponent),
    resolve: {
      messageIsoConfig: MessageIsoConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/message-iso-config-update.component').then(m => m.MessageIsoConfigUpdateComponent),
    resolve: {
      messageIsoConfig: MessageIsoConfigResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default messageIsoConfigRoute;
