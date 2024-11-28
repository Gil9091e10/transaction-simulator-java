import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getMerchants } from 'app/entities/merchant/merchant.reducer';
import { getEntities as getAcquirers } from 'app/entities/acquirer/acquirer.reducer';
import { createEntity, getEntity, reset, updateEntity } from './advice.reducer';

export const AdviceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const merchants = useAppSelector(state => state.merchant.entities);
  const acquirers = useAppSelector(state => state.acquirer.entities);
  const adviceEntity = useAppSelector(state => state.advice.entity);
  const loading = useAppSelector(state => state.advice.loading);
  const updating = useAppSelector(state => state.advice.updating);
  const updateSuccess = useAppSelector(state => state.advice.updateSuccess);

  const handleClose = () => {
    navigate(`/advice${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMerchants({}));
    dispatch(getAcquirers({}));
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
      ...adviceEntity,
      ...values,
      merchant: merchants.find(it => it.id.toString() === values.merchant?.toString()),
      acquirer: acquirers.find(it => it.id.toString() === values.acquirer?.toString()),
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
          ...adviceEntity,
          merchant: adviceEntity?.merchant?.id,
          acquirer: adviceEntity?.acquirer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simulatorApp.advice.home.createOrEditLabel" data-cy="AdviceCreateUpdateHeading">
            <Translate contentKey="simulatorApp.advice.home.createOrEditLabel">Create or edit a Advice</Translate>
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
                  id="advice-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simulatorApp.advice.code')}
                id="advice-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 10, message: translate('entity.validation.maxlength', { max: 10 }) },
                }}
              />
              <ValidatedField
                label={translate('simulatorApp.advice.name')}
                id="advice-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                id="advice-merchant"
                name="merchant"
                data-cy="merchant"
                label={translate('simulatorApp.advice.merchant')}
                type="select"
              >
                <option value="" key="0" />
                {merchants
                  ? merchants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="advice-acquirer"
                name="acquirer"
                data-cy="acquirer"
                label={translate('simulatorApp.advice.acquirer')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/advice" replace color="info">
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

export default AdviceUpdate;
