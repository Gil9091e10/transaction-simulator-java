import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getMessageIsoConfigs } from 'app/entities/message-iso-config/message-iso-config.reducer';
import { getEntities as getMessageFieldTypes } from 'app/entities/message-field-type/message-field-type.reducer';
import { createEntity, getEntity, reset, updateEntity } from './message-fields-config.reducer';

export const MessageFieldsConfigUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const messageIsoConfigs = useAppSelector(state => state.messageIsoConfig.entities);
  const messageFieldTypes = useAppSelector(state => state.messageFieldType.entities);
  const messageFieldsConfigEntity = useAppSelector(state => state.messageFieldsConfig.entity);
  const loading = useAppSelector(state => state.messageFieldsConfig.loading);
  const updating = useAppSelector(state => state.messageFieldsConfig.updating);
  const updateSuccess = useAppSelector(state => state.messageFieldsConfig.updateSuccess);

  const handleClose = () => {
    navigate(`/message-fields-config${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMessageIsoConfigs({}));
    dispatch(getMessageFieldTypes({}));
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
    if (values.fieldLength !== undefined && typeof values.fieldLength !== 'number') {
      values.fieldLength = Number(values.fieldLength);
    }

    const entity = {
      ...messageFieldsConfigEntity,
      ...values,
      messageIsoConfig: messageIsoConfigs.find(it => it.id.toString() === values.messageIsoConfig?.toString()),
      messageFieldType: messageFieldTypes.find(it => it.id.toString() === values.messageFieldType?.toString()),
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
          ...messageFieldsConfigEntity,
          messageIsoConfig: messageFieldsConfigEntity?.messageIsoConfig?.id,
          messageFieldType: messageFieldsConfigEntity?.messageFieldType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simulatorApp.messageFieldsConfig.home.createOrEditLabel" data-cy="MessageFieldsConfigCreateUpdateHeading">
            <Translate contentKey="simulatorApp.messageFieldsConfig.home.createOrEditLabel">Create or edit a MessageFieldsConfig</Translate>
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
                  id="message-fields-config-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simulatorApp.messageFieldsConfig.name')}
                id="message-fields-config-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('simulatorApp.messageFieldsConfig.fieldLength')}
                id="message-fields-config-fieldLength"
                name="fieldLength"
                data-cy="fieldLength"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="message-fields-config-messageIsoConfig"
                name="messageIsoConfig"
                data-cy="messageIsoConfig"
                label={translate('simulatorApp.messageFieldsConfig.messageIsoConfig')}
                type="select"
              >
                <option value="" key="0" />
                {messageIsoConfigs
                  ? messageIsoConfigs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="message-fields-config-messageFieldType"
                name="messageFieldType"
                data-cy="messageFieldType"
                label={translate('simulatorApp.messageFieldsConfig.messageFieldType')}
                type="select"
              >
                <option value="" key="0" />
                {messageFieldTypes
                  ? messageFieldTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/message-fields-config" replace color="info">
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

export default MessageFieldsConfigUpdate;
