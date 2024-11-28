import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './message-type-indicator.reducer';

export const MessageTypeIndicatorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const messageTypeIndicatorEntity = useAppSelector(state => state.messageTypeIndicator.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="messageTypeIndicatorDetailsHeading">
          <Translate contentKey="simulatorApp.messageTypeIndicator.detail.title">MessageTypeIndicator</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{messageTypeIndicatorEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simulatorApp.messageTypeIndicator.name">Name</Translate>
            </span>
          </dt>
          <dd>{messageTypeIndicatorEntity.name}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="simulatorApp.messageTypeIndicator.code">Code</Translate>
            </span>
          </dt>
          <dd>{messageTypeIndicatorEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/message-type-indicator" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/message-type-indicator/${messageTypeIndicatorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MessageTypeIndicatorDetail;
