import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './merchant.reducer';

export const MerchantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const merchantEntity = useAppSelector(state => state.merchant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="merchantDetailsHeading">
          <Translate contentKey="simulatorApp.merchant.detail.title">Merchant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{merchantEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simulatorApp.merchant.name">Name</Translate>
            </span>
          </dt>
          <dd>{merchantEntity.name}</dd>
          <dt>
            <span id="mcc">
              <Translate contentKey="simulatorApp.merchant.mcc">Mcc</Translate>
            </span>
          </dt>
          <dd>{merchantEntity.mcc}</dd>
          <dt>
            <span id="postalCode">
              <Translate contentKey="simulatorApp.merchant.postalCode">Postal Code</Translate>
            </span>
          </dt>
          <dd>{merchantEntity.postalCode}</dd>
          <dt>
            <span id="website">
              <Translate contentKey="simulatorApp.merchant.website">Website</Translate>
            </span>
          </dt>
          <dd>{merchantEntity.website}</dd>
        </dl>
        <Button tag={Link} to="/merchant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/merchant/${merchantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MerchantDetail;
