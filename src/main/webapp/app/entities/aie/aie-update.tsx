import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './aie.reducer';

export const AIEUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const aIEEntity = useAppSelector(state => state.reactsample.aIE.entity);
  const loading = useAppSelector(state => state.reactsample.aIE.loading);
  const updating = useAppSelector(state => state.reactsample.aIE.updating);
  const updateSuccess = useAppSelector(state => state.reactsample.aIE.updateSuccess);

  const handleClose = () => {
    navigate(`/aie${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);
    if (values.rate !== undefined && typeof values.rate !== 'number') {
      values.rate = Number(values.rate);
    }

    const entity = {
      ...aIEEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
        }
      : {
          ...aIEEntity,
          createdAt: convertDateTimeFromServer(aIEEntity.createdAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="reactSampleApp.aIE.home.createOrEditLabel" data-cy="AIECreateUpdateHeading">
            <Translate contentKey="reactSampleApp.aIE.home.createOrEditLabel">Create or edit a AIE</Translate>
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
                  id="aie-id"
                  label={translate('reactSampleApp.aIE.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('reactSampleApp.aIE.name')}
                id="aie-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('reactSampleApp.aIE.type')}
                id="aie-type"
                name="type"
                data-cy="type"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('reactSampleApp.aIE.description')}
                id="aie-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('reactSampleApp.aIE.createdAt')}
                id="aie-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('reactSampleApp.aIE.createdBy')}
                id="aie-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('reactSampleApp.aIE.icon')} id="aie-icon" name="icon" data-cy="icon" type="text" />
              <ValidatedField
                label={translate('reactSampleApp.aIE.version')}
                id="aie-version"
                name="version"
                data-cy="version"
                type="text"
              />
              <ValidatedField
                label={translate('reactSampleApp.aIE.category')}
                id="aie-category"
                name="category"
                data-cy="category"
                type="text"
              />
              <ValidatedField label={translate('reactSampleApp.aIE.rate')} id="aie-rate" name="rate" data-cy="rate" type="text" />
              <ValidatedField
                label={translate('reactSampleApp.aIE.aieMetadata')}
                id="aie-aieMetadata"
                name="aieMetadata"
                data-cy="aieMetadata"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('reactSampleApp.aIE.userID')}
                id="aie-userID"
                name="userID"
                data-cy="userID"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('reactSampleApp.aIE.isPublic')}
                id="aie-isPublic"
                name="isPublic"
                data-cy="isPublic"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('reactSampleApp.aIE.organizationName')}
                id="aie-organizationName"
                name="organizationName"
                data-cy="organizationName"
                type="text"
              />
              <ValidatedField
                label={translate('reactSampleApp.aIE.tenantID')}
                id="aie-tenantID"
                name="tenantID"
                data-cy="tenantID"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/aie" replace color="info">
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

export default AIEUpdate;
