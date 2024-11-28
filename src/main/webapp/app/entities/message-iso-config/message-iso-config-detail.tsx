import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './message-iso-config.reducer';

export const MessageIsoConfigDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const messageIsoConfigEntity = useAppSelector(state => state.messageIsoConfig.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="messageIsoConfigDetailsHeading">
          <Translate contentKey="simulatorApp.messageIsoConfig.detail.title">MessageIsoConfig</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{messageIsoConfigEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simulatorApp.messageIsoConfig.name">Name</Translate>
            </span>
          </dt>
          <dd>{messageIsoConfigEntity.name}</dd>
          <dt>
            <span id="filename">
              <Translate contentKey="simulatorApp.messageIsoConfig.filename">Filename</Translate>
            </span>
          </dt>
          <dd>{messageIsoConfigEntity.filename}</dd>
          <dt>
            <Translate contentKey="simulatorApp.messageIsoConfig.acquirer">Acquirer</Translate>
          </dt>
          <dd>{messageIsoConfigEntity.acquirer ? messageIsoConfigEntity.acquirer.id : ''}</dd>
          <dt>
            <Translate contentKey="simulatorApp.messageIsoConfig.messageTypeIndicator">Message Type Indicator</Translate>
          </dt>
          <dd>{messageIsoConfigEntity.messageTypeIndicator ? messageIsoConfigEntity.messageTypeIndicator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/message-iso-config" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/message-iso-config/${messageIsoConfigEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MessageIsoConfigDetail;
