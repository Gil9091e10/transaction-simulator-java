import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './acquirer.reducer';

export const AcquirerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const acquirerEntity = useAppSelector(state => state.acquirer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="acquirerDetailsHeading">
          <Translate contentKey="simulatorApp.acquirer.detail.title">Acquirer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{acquirerEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simulatorApp.acquirer.name">Name</Translate>
            </span>
          </dt>
          <dd>{acquirerEntity.name}</dd>
          <dt>
            <span id="socketUrl">
              <Translate contentKey="simulatorApp.acquirer.socketUrl">Socket Url</Translate>
            </span>
          </dt>
          <dd>{acquirerEntity.socketUrl}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="simulatorApp.acquirer.email">Email</Translate>
            </span>
          </dt>
          <dd>{acquirerEntity.email}</dd>
        </dl>
        <Button tag={Link} to="/acquirer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/acquirer/${acquirerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AcquirerDetail;
