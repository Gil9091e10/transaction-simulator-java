import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getFieldTypes } from 'app/entities/field-type/field-type.reducer';
import { createEntity, getEntity, reset, updateEntity } from './message-field-type.reducer';

export const MessageFieldTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const fieldTypes = useAppSelector(state => state.fieldType.entities);
  const messageFieldTypeEntity = useAppSelector(state => state.messageFieldType.entity);
  const loading = useAppSelector(state => state.messageFieldType.loading);
  const updating = useAppSelector(state => state.messageFieldType.updating);
  const updateSuccess = useAppSelector(state => state.messageFieldType.updateSuccess);

  const handleClose = () => {
    navigate(`/message-field-type${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFieldTypes({}));
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
      ...messageFieldTypeEntity,
      ...values,
      fieldType: fieldTypes.find(it => it.id.toString() === values.fieldType?.toString()),
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
          ...messageFieldTypeEntity,
          fieldType: messageFieldTypeEntity?.fieldType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simulatorApp.messageFieldType.home.createOrEditLabel" data-cy="MessageFieldTypeCreateUpdateHeading">
            <Translate contentKey="simulatorApp.messageFieldType.home.createOrEditLabel">Create or edit a MessageFieldType</Translate>
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
                  id="message-field-type-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simulatorApp.messageFieldType.name')}
                id="message-field-type-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="message-field-type-fieldType"
                name="fieldType"
                data-cy="fieldType"
                label={translate('simulatorApp.messageFieldType.fieldType')}
                type="select"
              >
                <option value="" key="0" />
                {fieldTypes
                  ? fieldTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/message-field-type" replace color="info">
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

export default MessageFieldTypeUpdate;
