import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './advice.reducer';

export const AdviceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const adviceEntity = useAppSelector(state => state.advice.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="adviceDetailsHeading">
          <Translate contentKey="simulatorApp.advice.detail.title">Advice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{adviceEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="simulatorApp.advice.code">Code</Translate>
            </span>
          </dt>
          <dd>{adviceEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simulatorApp.advice.name">Name</Translate>
            </span>
          </dt>
          <dd>{adviceEntity.name}</dd>
          <dt>
            <Translate contentKey="simulatorApp.advice.merchant">Merchant</Translate>
          </dt>
          <dd>{adviceEntity.merchant ? adviceEntity.merchant.id : ''}</dd>
          <dt>
            <Translate contentKey="simulatorApp.advice.acquirer">Acquirer</Translate>
          </dt>
          <dd>{adviceEntity.acquirer ? adviceEntity.acquirer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/advice" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/advice/${adviceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AdviceDetail;
