import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CreditCard from './credit-card';
import CreditCardDetail from './credit-card-detail';
import CreditCardUpdate from './credit-card-update';
import CreditCardDeleteDialog from './credit-card-delete-dialog';

const CreditCardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CreditCard />} />
    <Route path="new" element={<CreditCardUpdate />} />
    <Route path=":id">
      <Route index element={<CreditCardDetail />} />
      <Route path="edit" element={<CreditCardUpdate />} />
      <Route path="delete" element={<CreditCardDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CreditCardRoutes;
