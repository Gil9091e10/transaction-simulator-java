import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './message-field-type.reducer';

export const MessageFieldTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const messageFieldTypeEntity = useAppSelector(state => state.messageFieldType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="messageFieldTypeDetailsHeading">
          <Translate contentKey="simulatorApp.messageFieldType.detail.title">MessageFieldType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{messageFieldTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simulatorApp.messageFieldType.name">Name</Translate>
            </span>
          </dt>
          <dd>{messageFieldTypeEntity.name}</dd>
          <dt>
            <Translate contentKey="simulatorApp.messageFieldType.fieldType">Field Type</Translate>
          </dt>
          <dd>{messageFieldTypeEntity.fieldType ? messageFieldTypeEntity.fieldType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/message-field-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/message-field-type/${messageFieldTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MessageFieldTypeDetail;
