import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAcquirers } from 'app/entities/acquirer/acquirer.reducer';
import { getEntities as getMessageTypeIndicators } from 'app/entities/message-type-indicator/message-type-indicator.reducer';
import { createEntity, getEntity, reset, updateEntity } from './message-iso-config.reducer';

export const MessageIsoConfigUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const acquirers = useAppSelector(state => state.acquirer.entities);
  const messageTypeIndicators = useAppSelector(state => state.messageTypeIndicator.entities);
  const messageIsoConfigEntity = useAppSelector(state => state.messageIsoConfig.entity);
  const loading = useAppSelector(state => state.messageIsoConfig.loading);
  const updating = useAppSelector(state => state.messageIsoConfig.updating);
  const updateSuccess = useAppSelector(state => state.messageIsoConfig.updateSuccess);

  const handleClose = () => {
    navigate(`/message-iso-config${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAcquirers({}));
    dispatch(getMessageTypeIndicators({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...messageIsoConfigEntity,
      ...values,
      acquirer: acquirers.find(it => it.id.toString() === values.acquirer?.toString()),
      messageTypeIndicator: messageTypeIndicators.find(it => it.id.toString() === values.messageTypeIndicator?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...messageIsoConfigEntity,
          acquirer: messageIsoConfigEntity?.acquirer?.id,
          messageTypeIndicator: messageIsoConfigEntity?.messageTypeIndicator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simulatorApp.messageIsoConfig.home.createOrEditLabel" data-cy="MessageIsoConfigCreateUpdateHeading">
            <Translate contentKey="simulatorApp.messageIsoConfig.home.createOrEditLabel">Create or edit a MessageIsoConfig</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="message-iso-config-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simulatorApp.messageIsoConfig.name')}
                id="message-iso-config-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('simulatorApp.messageIsoConfig.filename')}
                id="message-iso-config-filename"
                name="filename"
                data-cy="filename"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                id="message-iso-config-acquirer"
                name="acquirer"
                data-cy="acquirer"
                label={translate('simulatorApp.messageIsoConfig.acquirer')}
                type="select"
              >
                <option value="" key="0" />
                {acquirers
                  ? acquirers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="message-iso-config-messageTypeIndicator"
                name="messageTypeIndicator"
                data-cy="messageTypeIndicator"
                label={translate('simulatorApp.messageIsoConfig.messageTypeIndicator')}
                type="select"
              >
                <option value="" key="0" />
                {messageTypeIndicators
                  ? messageTypeIndicators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/message-iso-config" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MessageIsoConfigUpdate;
