import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './transaction-type.reducer';

export const TransactionTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const transactionTypeEntity = useAppSelector(state => state.transactionType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transactionTypeDetailsHeading">
          <Translate contentKey="simulatorApp.transactionType.detail.title">TransactionType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transactionTypeEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="simulatorApp.transactionType.code">Code</Translate>
            </span>
          </dt>
          <dd>{transactionTypeEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simulatorApp.transactionType.name">Name</Translate>
            </span>
          </dt>
          <dd>{transactionTypeEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/transaction-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transaction-type/${transactionTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransactionTypeDetail;
