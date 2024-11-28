import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Advice from './advice';
import AdviceDetail from './advice-detail';
import AdviceUpdate from './advice-update';
import AdviceDeleteDialog from './advice-delete-dialog';

const AdviceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Advice />} />
    <Route path="new" element={<AdviceUpdate />} />
    <Route path=":id">
      <Route index element={<AdviceDetail />} />
      <Route path="edit" element={<AdviceUpdate />} />
      <Route path="delete" element={<AdviceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AdviceRoutes;
