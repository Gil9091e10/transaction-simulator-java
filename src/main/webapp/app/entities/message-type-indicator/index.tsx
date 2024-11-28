import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MessageTypeIndicator from './message-type-indicator';
import MessageTypeIndicatorDetail from './message-type-indicator-detail';
import MessageTypeIndicatorUpdate from './message-type-indicator-update';
import MessageTypeIndicatorDeleteDialog from './message-type-indicator-delete-dialog';

const MessageTypeIndicatorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MessageTypeIndicator />} />
    <Route path="new" element={<MessageTypeIndicatorUpdate />} />
    <Route path=":id">
      <Route index element={<MessageTypeIndicatorDetail />} />
      <Route path="edit" element={<MessageTypeIndicatorUpdate />} />
      <Route path="delete" element={<MessageTypeIndicatorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MessageTypeIndicatorRoutes;
