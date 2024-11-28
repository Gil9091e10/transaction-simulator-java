import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AccountBank from './account-bank';
import AccountBankDetail from './account-bank-detail';
import AccountBankUpdate from './account-bank-update';
import AccountBankDeleteDialog from './account-bank-delete-dialog';

const AccountBankRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AccountBank />} />
    <Route path="new" element={<AccountBankUpdate />} />
    <Route path=":id">
      <Route index element={<AccountBankDetail />} />
      <Route path="edit" element={<AccountBankUpdate />} />
      <Route path="delete" element={<AccountBankDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AccountBankRoutes;
