import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCurrencies } from 'app/entities/currency/currency.reducer';
import { createEntity, getEntity, reset, updateEntity } from './account-bank.reducer';

export const AccountBankUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const currencies = useAppSelector(state => state.currency.entities);
  const accountBankEntity = useAppSelector(state => state.accountBank.entity);
  const loading = useAppSelector(state => state.accountBank.loading);
  const updating = useAppSelector(state => state.accountBank.updating);
  const updateSuccess = useAppSelector(state => state.accountBank.updateSuccess);

  const handleClose = () => {
    navigate(`/account-bank${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCurrencies({}));
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
    if (values.number !== undefined && typeof values.number !== 'number') {
      values.number = Number(values.number);
    }
    if (values.balance !== undefined && typeof values.balance !== 'number') {
      values.balance = Number(values.balance);
    }

    const entity = {
      ...accountBankEntity,
      ...values,
      currency: currencies.find(it => it.id.toString() === values.currency?.toString()),
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
          ...accountBankEntity,
          currency: accountBankEntity?.currency?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simulatorApp.accountBank.home.createOrEditLabel" data-cy="AccountBankCreateUpdateHeading">
            <Translate contentKey="simulatorApp.accountBank.home.createOrEditLabel">Create or edit a AccountBank</Translate>
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
                  id="account-bank-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simulatorApp.accountBank.number')}
                id="account-bank-number"
                name="number"
                data-cy="number"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('simulatorApp.accountBank.numberIBAN')}
                id="account-bank-numberIBAN"
                name="numberIBAN"
                data-cy="numberIBAN"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('simulatorApp.accountBank.balance')}
                id="account-bank-balance"
                name="balance"
                data-cy="balance"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="account-bank-currency"
                name="currency"
                data-cy="currency"
                label={translate('simulatorApp.accountBank.currency')}
                type="select"
              >
                <option value="" key="0" />
                {currencies
                  ? currencies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/account-bank" replace color="info">
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

export default AccountBankUpdate;
