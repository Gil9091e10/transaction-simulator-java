import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/message-type-indicator">
        <Translate contentKey="global.menu.entities.messageTypeIndicator" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/message-iso-config">
        <Translate contentKey="global.menu.entities.messageIsoConfig" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/message-fields-config">
        <Translate contentKey="global.menu.entities.messageFieldsConfig" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/message-field-type">
        <Translate contentKey="global.menu.entities.messageFieldType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/field-type">
        <Translate contentKey="global.menu.entities.fieldType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/currency">
        <Translate contentKey="global.menu.entities.currency" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/acquirer">
        <Translate contentKey="global.menu.entities.acquirer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/merchant">
        <Translate contentKey="global.menu.entities.merchant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/issuer">
        <Translate contentKey="global.menu.entities.issuer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/advice">
        <Translate contentKey="global.menu.entities.advice" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/account-bank">
        <Translate contentKey="global.menu.entities.accountBank" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/card-type">
        <Translate contentKey="global.menu.entities.cardType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/transaction-type">
        <Translate contentKey="global.menu.entities.transactionType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/card">
        <Translate contentKey="global.menu.entities.card" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/transaction">
        <Translate contentKey="global.menu.entities.transaction" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/debit-card">
        <Translate contentKey="global.menu.entities.debitCard" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/credit-card">
        <Translate contentKey="global.menu.entities.creditCard" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
