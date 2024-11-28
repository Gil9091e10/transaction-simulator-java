import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './card.reducer';

export const CardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cardEntity = useAppSelector(state => state.card.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cardDetailsHeading">
          <Translate contentKey="simulatorApp.card.detail.title">Card</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cardEntity.id}</dd>
          <dt>
            <span id="number">
              <Translate contentKey="simulatorApp.card.number">Number</Translate>
            </span>
          </dt>
          <dd>{cardEntity.number}</dd>
          <dt>
            <span id="expirationDate">
              <Translate contentKey="simulatorApp.card.expirationDate">Expiration Date</Translate>
            </span>
          </dt>
          <dd>
            {cardEntity.expirationDate ? <TextFormat value={cardEntity.expirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="verificationValue">
              <Translate contentKey="simulatorApp.card.verificationValue">Verification Value</Translate>
            </span>
          </dt>
          <dd>{cardEntity.verificationValue}</dd>
          <dt>
            <Translate contentKey="simulatorApp.card.accountBank">Account Bank</Translate>
          </dt>
          <dd>{cardEntity.accountBank ? cardEntity.accountBank.id : ''}</dd>
          <dt>
            <Translate contentKey="simulatorApp.card.cardType">Card Type</Translate>
          </dt>
          <dd>{cardEntity.cardType ? cardEntity.cardType.id : ''}</dd>
          <dt>
            <Translate contentKey="simulatorApp.card.issuer">Issuer</Translate>
          </dt>
          <dd>{cardEntity.issuer ? cardEntity.issuer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/card" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/card/${cardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CardDetail;
