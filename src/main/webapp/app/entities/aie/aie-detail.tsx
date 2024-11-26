import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './aie.reducer';

export const AIEDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const aIEEntity = useAppSelector(state => state.reactsample.aIE.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aIEDetailsHeading">
          <Translate contentKey="reactSampleApp.aIE.detail.title">AIE</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="reactSampleApp.aIE.id">Id</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="reactSampleApp.aIE.name">Name</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.name}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="reactSampleApp.aIE.type">Type</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.type}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="reactSampleApp.aIE.description">Description</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.description}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="reactSampleApp.aIE.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.createdAt ? <TextFormat value={aIEEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="reactSampleApp.aIE.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.createdBy}</dd>
          <dt>
            <span id="icon">
              <Translate contentKey="reactSampleApp.aIE.icon">Icon</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.icon}</dd>
          <dt>
            <span id="version">
              <Translate contentKey="reactSampleApp.aIE.version">Version</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.version}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="reactSampleApp.aIE.category">Category</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.category}</dd>
          <dt>
            <span id="rate">
              <Translate contentKey="reactSampleApp.aIE.rate">Rate</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.rate}</dd>
          <dt>
            <span id="aieMetadata">
              <Translate contentKey="reactSampleApp.aIE.aieMetadata">Aie Metadata</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.aieMetadata}</dd>
          <dt>
            <span id="userID">
              <Translate contentKey="reactSampleApp.aIE.userID">User ID</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.userID}</dd>
          <dt>
            <span id="isPublic">
              <Translate contentKey="reactSampleApp.aIE.isPublic">Is Public</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.isPublic ? 'true' : 'false'}</dd>
          <dt>
            <span id="organizationName">
              <Translate contentKey="reactSampleApp.aIE.organizationName">Organization Name</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.organizationName}</dd>
          <dt>
            <span id="tenantID">
              <Translate contentKey="reactSampleApp.aIE.tenantID">Tenant ID</Translate>
            </span>
          </dt>
          <dd>{aIEEntity.tenantID}</dd>
        </dl>
        <Button tag={Link} to="/aie" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aie/${aIEEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AIEDetail;
