import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './message-fields-config.reducer';

export const MessageFieldsConfigDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const messageFieldsConfigEntity = useAppSelector(state => state.messageFieldsConfig.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="messageFieldsConfigDetailsHeading">
          <Translate contentKey="simulatorApp.messageFieldsConfig.detail.title">MessageFieldsConfig</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{messageFieldsConfigEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simulatorApp.messageFieldsConfig.name">Name</Translate>
            </span>
          </dt>
          <dd>{messageFieldsConfigEntity.name}</dd>
          <dt>
            <span id="fieldLength">
              <Translate contentKey="simulatorApp.messageFieldsConfig.fieldLength">Field Length</Translate>
            </span>
          </dt>
          <dd>{messageFieldsConfigEntity.fieldLength}</dd>
          <dt>
            <Translate contentKey="simulatorApp.messageFieldsConfig.messageIsoConfig">Message Iso Config</Translate>
          </dt>
          <dd>{messageFieldsConfigEntity.messageIsoConfig ? messageFieldsConfigEntity.messageIsoConfig.id : ''}</dd>
          <dt>
            <Translate contentKey="simulatorApp.messageFieldsConfig.messageFieldType">Message Field Type</Translate>
          </dt>
          <dd>{messageFieldsConfigEntity.messageFieldType ? messageFieldsConfigEntity.messageFieldType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/message-fields-config" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/message-fields-config/${messageFieldsConfigEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MessageFieldsConfigDetail;
