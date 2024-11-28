import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MessageTypeIndicator from './message-type-indicator';
import MessageIsoConfig from './message-iso-config';
import MessageFieldsConfig from './message-fields-config';
import MessageFieldType from './message-field-type';
import FieldType from './field-type';
import Currency from './currency';
import Acquirer from './acquirer';
import Merchant from './merchant';
import Issuer from './issuer';
import Advice from './advice';
import AccountBank from './account-bank';
import CardType from './card-type';
import TransactionType from './transaction-type';
import Card from './card';
import Transaction from './transaction';
import DebitCard from './debit-card';
import CreditCard from './credit-card';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="message-type-indicator/*" element={<MessageTypeIndicator />} />
        <Route path="message-iso-config/*" element={<MessageIsoConfig />} />
        <Route path="message-fields-config/*" element={<MessageFieldsConfig />} />
        <Route path="message-field-type/*" element={<MessageFieldType />} />
        <Route path="field-type/*" element={<FieldType />} />
        <Route path="currency/*" element={<Currency />} />
        <Route path="acquirer/*" element={<Acquirer />} />
        <Route path="merchant/*" element={<Merchant />} />
        <Route path="issuer/*" element={<Issuer />} />
        <Route path="advice/*" element={<Advice />} />
        <Route path="account-bank/*" element={<AccountBank />} />
        <Route path="card-type/*" element={<CardType />} />
        <Route path="transaction-type/*" element={<TransactionType />} />
        <Route path="card/*" element={<Card />} />
        <Route path="transaction/*" element={<Transaction />} />
        <Route path="debit-card/*" element={<DebitCard />} />
        <Route path="credit-card/*" element={<CreditCard />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
