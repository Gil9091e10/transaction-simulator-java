import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Acquirer from './acquirer';
import AcquirerDetail from './acquirer-detail';
import AcquirerUpdate from './acquirer-update';
import AcquirerDeleteDialog from './acquirer-delete-dialog';

const AcquirerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Acquirer />} />
    <Route path="new" element={<AcquirerUpdate />} />
    <Route path=":id">
      <Route index element={<AcquirerDetail />} />
      <Route path="edit" element={<AcquirerUpdate />} />
      <Route path="delete" element={<AcquirerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AcquirerRoutes;
