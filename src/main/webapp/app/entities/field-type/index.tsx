import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FieldType from './field-type';
import FieldTypeDetail from './field-type-detail';
import FieldTypeUpdate from './field-type-update';
import FieldTypeDeleteDialog from './field-type-delete-dialog';

const FieldTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FieldType />} />
    <Route path="new" element={<FieldTypeUpdate />} />
    <Route path=":id">
      <Route index element={<FieldTypeDetail />} />
      <Route path="edit" element={<FieldTypeUpdate />} />
      <Route path="delete" element={<FieldTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FieldTypeRoutes;
