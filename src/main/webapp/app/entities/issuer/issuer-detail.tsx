import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './issuer.reducer';

export const IssuerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const issuerEntity = useAppSelector(state => state.issuer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="issuerDetailsHeading">
          <Translate contentKey="simulatorApp.issuer.detail.title">Issuer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{issuerEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="simulatorApp.issuer.code">Code</Translate>
            </span>
          </dt>
          <dd>{issuerEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simulatorApp.issuer.name">Name</Translate>
            </span>
          </dt>
          <dd>{issuerEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/issuer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/issuer/${issuerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IssuerDetail;
