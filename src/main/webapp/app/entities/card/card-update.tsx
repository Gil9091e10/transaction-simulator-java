import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAccountBanks } from 'app/entities/account-bank/account-bank.reducer';
import { getEntities as getCardTypes } from 'app/entities/card-type/card-type.reducer';
import { getEntities as getIssuers } from 'app/entities/issuer/issuer.reducer';
import { createEntity, getEntity, reset, updateEntity } from './card.reducer';

export const CardUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const accountBanks = useAppSelector(state => state.accountBank.entities);
  const cardTypes = useAppSelector(state => state.cardType.entities);
  const issuers = useAppSelector(state => state.issuer.entities);
  const cardEntity = useAppSelector(state => state.card.entity);
  const loading = useAppSelector(state => state.card.loading);
  const updating = useAppSelector(state => state.card.updating);
  const updateSuccess = useAppSelector(state => state.card.updateSuccess);

  const handleClose = () => {
    navigate(`/card${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAccountBanks({}));
    dispatch(getCardTypes({}));
    dispatch(getIssuers({}));
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
    if (values.verificationValue !== undefined && typeof values.verificationValue !== 'number') {
      values.verificationValue = Number(values.verificationValue);
    }

    const entity = {
      ...cardEntity,
      ...values,
      accountBank: accountBanks.find(it => it.id.toString() === values.accountBank?.toString()),
      cardType: cardTypes.find(it => it.id.toString() === values.cardType?.toString()),
      issuer: issuers.find(it => it.id.toString() === values.issuer?.toString()),
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
          ...cardEntity,
          accountBank: cardEntity?.accountBank?.id,
          cardType: cardEntity?.cardType?.id,
          issuer: cardEntity?.issuer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simulatorApp.card.home.createOrEditLabel" data-cy="CardCreateUpdateHeading">
            <Translate contentKey="simulatorApp.card.home.createOrEditLabel">Create or edit a Card</Translate>
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
                  id="card-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simulatorApp.card.number')}
                id="card-number"
                name="number"
                data-cy="number"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('simulatorApp.card.expirationDate')}
                id="card-expirationDate"
                name="expirationDate"
                data-cy="expirationDate"
                type="date"
              />
              <ValidatedField
                label={translate('simulatorApp.card.verificationValue')}
                id="card-verificationValue"
                name="verificationValue"
                data-cy="verificationValue"
                type="text"
              />
              <ValidatedField
                id="card-accountBank"
                name="accountBank"
                data-cy="accountBank"
                label={translate('simulatorApp.card.accountBank')}
                type="select"
              >
                <option value="" key="0" />
                {accountBanks
                  ? accountBanks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="card-cardType"
                name="cardType"
                data-cy="cardType"
                label={translate('simulatorApp.card.cardType')}
                type="select"
              >
                <option value="" key="0" />
                {cardTypes
                  ? cardTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="card-issuer" name="issuer" data-cy="issuer" label={translate('simulatorApp.card.issuer')} type="select">
                <option value="" key="0" />
                {issuers
                  ? issuers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/card" replace color="info">
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

export default CardUpdate;
