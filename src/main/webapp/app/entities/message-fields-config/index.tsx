import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MessageFieldsConfig from './message-fields-config';
import MessageFieldsConfigDetail from './message-fields-config-detail';
import MessageFieldsConfigUpdate from './message-fields-config-update';
import MessageFieldsConfigDeleteDialog from './message-fields-config-delete-dialog';

const MessageFieldsConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MessageFieldsConfig />} />
    <Route path="new" element={<MessageFieldsConfigUpdate />} />
    <Route path=":id">
      <Route index element={<MessageFieldsConfigDetail />} />
      <Route path="edit" element={<MessageFieldsConfigUpdate />} />
      <Route path="delete" element={<MessageFieldsConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MessageFieldsConfigRoutes;
