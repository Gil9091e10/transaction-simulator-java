import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'simulatorApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'message-type-indicator',
    data: { pageTitle: 'simulatorApp.messageTypeIndicator.home.title' },
    loadChildren: () => import('./message-type-indicator/message-type-indicator.routes'),
  },
  {
    path: 'message-iso-config',
    data: { pageTitle: 'simulatorApp.messageIsoConfig.home.title' },
    loadChildren: () => import('./message-iso-config/message-iso-config.routes'),
  },
  {
    path: 'currency',
    data: { pageTitle: 'simulatorApp.currency.home.title' },
    loadChildren: () => import('./currency/currency.routes'),
  },
  {
    path: 'acquirer',
    data: { pageTitle: 'simulatorApp.acquirer.home.title' },
    loadChildren: () => import('./acquirer/acquirer.routes'),
  },
  {
    path: 'merchant',
    data: { pageTitle: 'simulatorApp.merchant.home.title' },
    loadChildren: () => import('./merchant/merchant.routes'),
  },
  {
    path: 'issuer',
    data: { pageTitle: 'simulatorApp.issuer.home.title' },
    loadChildren: () => import('./issuer/issuer.routes'),
  },
  {
    path: 'advice',
    data: { pageTitle: 'simulatorApp.advice.home.title' },
    loadChildren: () => import('./advice/advice.routes'),
  },
  {
    path: 'account-bank',
    data: { pageTitle: 'simulatorApp.accountBank.home.title' },
    loadChildren: () => import('./account-bank/account-bank.routes'),
  },
  {
    path: 'card-type',
    data: { pageTitle: 'simulatorApp.cardType.home.title' },
    loadChildren: () => import('./card-type/card-type.routes'),
  },
  {
    path: 'transaction-type',
    data: { pageTitle: 'simulatorApp.transactionType.home.title' },
    loadChildren: () => import('./transaction-type/transaction-type.routes'),
  },
  {
    path: 'card',
    data: { pageTitle: 'simulatorApp.card.home.title' },
    loadChildren: () => import('./card/card.routes'),
  },
  {
    path: 'transaction',
    data: { pageTitle: 'simulatorApp.transaction.home.title' },
    loadChildren: () => import('./transaction/transaction.routes'),
  },
  {
    path: 'debit-card',
    data: { pageTitle: 'simulatorApp.debitCard.home.title' },
    loadChildren: () => import('./debit-card/debit-card.routes'),
  },
  {
    path: 'credit-card',
    data: { pageTitle: 'simulatorApp.creditCard.home.title' },
    loadChildren: () => import('./credit-card/credit-card.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
