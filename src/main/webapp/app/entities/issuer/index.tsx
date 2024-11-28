import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Issuer from './issuer';
import IssuerDetail from './issuer-detail';
import IssuerUpdate from './issuer-update';
import IssuerDeleteDialog from './issuer-delete-dialog';

const IssuerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Issuer />} />
    <Route path="new" element={<IssuerUpdate />} />
    <Route path=":id">
      <Route index element={<IssuerDetail />} />
      <Route path="edit" element={<IssuerUpdate />} />
      <Route path="delete" element={<IssuerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IssuerRoutes;
