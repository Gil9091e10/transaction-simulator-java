import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DebitCard from './debit-card';
import DebitCardDetail from './debit-card-detail';
import DebitCardUpdate from './debit-card-update';
import DebitCardDeleteDialog from './debit-card-delete-dialog';

const DebitCardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DebitCard />} />
    <Route path="new" element={<DebitCardUpdate />} />
    <Route path=":id">
      <Route index element={<DebitCardDetail />} />
      <Route path="edit" element={<DebitCardUpdate />} />
      <Route path="delete" element={<DebitCardDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DebitCardRoutes;
