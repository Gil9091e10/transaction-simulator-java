import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MessageIsoConfig from './message-iso-config';
import MessageIsoConfigDetail from './message-iso-config-detail';
import MessageIsoConfigUpdate from './message-iso-config-update';
import MessageIsoConfigDeleteDialog from './message-iso-config-delete-dialog';

const MessageIsoConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MessageIsoConfig />} />
    <Route path="new" element={<MessageIsoConfigUpdate />} />
    <Route path=":id">
      <Route index element={<MessageIsoConfigDetail />} />
      <Route path="edit" element={<MessageIsoConfigUpdate />} />
      <Route path="delete" element={<MessageIsoConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MessageIsoConfigRoutes;
