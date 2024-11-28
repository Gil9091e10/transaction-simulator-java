import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './credit-card.reducer';

export const CreditCardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const creditCardEntity = useAppSelector(state => state.creditCard.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="creditCardDetailsHeading">
          <Translate contentKey="simulatorApp.creditCard.detail.title">CreditCard</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{creditCardEntity.id}</dd>
          <dt>
            <span id="maxLimit">
              <Translate contentKey="simulatorApp.creditCard.maxLimit">Max Limit</Translate>
            </span>
          </dt>
          <dd>{creditCardEntity.maxLimit}</dd>
        </dl>
        <Button tag={Link} to="/credit-card" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/credit-card/${creditCardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CreditCardDetail;
