import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CardType from './card-type';
import CardTypeDetail from './card-type-detail';
import CardTypeUpdate from './card-type-update';
import CardTypeDeleteDialog from './card-type-delete-dialog';

const CardTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CardType />} />
    <Route path="new" element={<CardTypeUpdate />} />
    <Route path=":id">
      <Route index element={<CardTypeDetail />} />
      <Route path="edit" element={<CardTypeUpdate />} />
      <Route path="delete" element={<CardTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CardTypeRoutes;
