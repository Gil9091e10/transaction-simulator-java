import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './account-bank.reducer';

export const AccountBankDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const accountBankEntity = useAppSelector(state => state.accountBank.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="accountBankDetailsHeading">
          <Translate contentKey="simulatorApp.accountBank.detail.title">AccountBank</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{accountBankEntity.id}</dd>
          <dt>
            <span id="number">
              <Translate contentKey="simulatorApp.accountBank.number">Number</Translate>
            </span>
          </dt>
          <dd>{accountBankEntity.number}</dd>
          <dt>
            <span id="numberIBAN">
              <Translate contentKey="simulatorApp.accountBank.numberIBAN">Number IBAN</Translate>
            </span>
          </dt>
          <dd>{accountBankEntity.numberIBAN}</dd>
          <dt>
            <span id="balance">
              <Translate contentKey="simulatorApp.accountBank.balance">Balance</Translate>
            </span>
          </dt>
          <dd>{accountBankEntity.balance}</dd>
          <dt>
            <Translate contentKey="simulatorApp.accountBank.currency">Currency</Translate>
          </dt>
          <dd>{accountBankEntity.currency ? accountBankEntity.currency.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/account-bank" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/account-bank/${accountBankEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AccountBankDetail;
