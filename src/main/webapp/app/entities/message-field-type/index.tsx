import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MessageFieldType from './message-field-type';
import MessageFieldTypeDetail from './message-field-type-detail';
import MessageFieldTypeUpdate from './message-field-type-update';
import MessageFieldTypeDeleteDialog from './message-field-type-delete-dialog';

const MessageFieldTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MessageFieldType />} />
    <Route path="new" element={<MessageFieldTypeUpdate />} />
    <Route path=":id">
      <Route index element={<MessageFieldTypeDetail />} />
      <Route path="edit" element={<MessageFieldTypeUpdate />} />
      <Route path="delete" element={<MessageFieldTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MessageFieldTypeRoutes;
