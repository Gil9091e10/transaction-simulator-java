import messageTypeIndicator from 'app/entities/message-type-indicator/message-type-indicator.reducer';
import messageIsoConfig from 'app/entities/message-iso-config/message-iso-config.reducer';
import messageFieldsConfig from 'app/entities/message-fields-config/message-fields-config.reducer';
import messageFieldType from 'app/entities/message-field-type/message-field-type.reducer';
import fieldType from 'app/entities/field-type/field-type.reducer';
import currency from 'app/entities/currency/currency.reducer';
import acquirer from 'app/entities/acquirer/acquirer.reducer';
import merchant from 'app/entities/merchant/merchant.reducer';
import issuer from 'app/entities/issuer/issuer.reducer';
import advice from 'app/entities/advice/advice.reducer';
import accountBank from 'app/entities/account-bank/account-bank.reducer';
import cardType from 'app/entities/card-type/card-type.reducer';
import transactionType from 'app/entities/transaction-type/transaction-type.reducer';
import card from 'app/entities/card/card.reducer';
import transaction from 'app/entities/transaction/transaction.reducer';
import debitCard from 'app/entities/debit-card/debit-card.reducer';
import creditCard from 'app/entities/credit-card/credit-card.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  messageTypeIndicator,
  messageIsoConfig,
  messageFieldsConfig,
  messageFieldType,
  fieldType,
  currency,
  acquirer,
  merchant,
  issuer,
  advice,
  accountBank,
  cardType,
  transactionType,
  card,
  transaction,
  debitCard,
  creditCard,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
