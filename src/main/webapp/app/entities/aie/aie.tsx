import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './aie.reducer';

export const AIE = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const aIEList = useAppSelector(state => state.reactsample.aIE.entities);
  const loading = useAppSelector(state => state.reactsample.aIE.loading);
  const totalItems = useAppSelector(state => state.reactsample.aIE.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="aie-heading" data-cy="AIEHeading">
        <Translate contentKey="reactSampleApp.aIE.home.title">AIES</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="reactSampleApp.aIE.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/aie/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="reactSampleApp.aIE.home.createLabel">Create new AIE</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aIEList && aIEList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="reactSampleApp.aIE.id">Id</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="reactSampleApp.aIE.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="reactSampleApp.aIE.type">Type</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="reactSampleApp.aIE.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="reactSampleApp.aIE.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="reactSampleApp.aIE.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('icon')}>
                  <Translate contentKey="reactSampleApp.aIE.icon">Icon</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('icon')} />
                </th>
                <th className="hand" onClick={sort('version')}>
                  <Translate contentKey="reactSampleApp.aIE.version">Version</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('version')} />
                </th>
                <th className="hand" onClick={sort('category')}>
                  <Translate contentKey="reactSampleApp.aIE.category">Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('category')} />
                </th>
                <th className="hand" onClick={sort('rate')}>
                  <Translate contentKey="reactSampleApp.aIE.rate">Rate</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('rate')} />
                </th>
                <th className="hand" onClick={sort('aieMetadata')}>
                  <Translate contentKey="reactSampleApp.aIE.aieMetadata">Aie Metadata</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('aieMetadata')} />
                </th>
                <th className="hand" onClick={sort('userID')}>
                  <Translate contentKey="reactSampleApp.aIE.userID">User ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userID')} />
                </th>
                <th className="hand" onClick={sort('isPublic')}>
                  <Translate contentKey="reactSampleApp.aIE.isPublic">Is Public</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isPublic')} />
                </th>
                <th className="hand" onClick={sort('organizationName')}>
                  <Translate contentKey="reactSampleApp.aIE.organizationName">Organization Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('organizationName')} />
                </th>
                <th className="hand" onClick={sort('tenantID')}>
                  <Translate contentKey="reactSampleApp.aIE.tenantID">Tenant ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tenantID')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {aIEList.map((aIE, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/aie/${aIE.id}`} color="link" size="sm">
                      {aIE.id}
                    </Button>
                  </td>
                  <td>{aIE.name}</td>
                  <td>{aIE.type}</td>
                  <td>{aIE.description}</td>
                  <td>{aIE.createdAt ? <TextFormat type="date" value={aIE.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{aIE.createdBy}</td>
                  <td>{aIE.icon}</td>
                  <td>{aIE.version}</td>
                  <td>{aIE.category}</td>
                  <td>{aIE.rate}</td>
                  <td>{aIE.aieMetadata}</td>
                  <td>{aIE.userID}</td>
                  <td>{aIE.isPublic ? 'true' : 'false'}</td>
                  <td>{aIE.organizationName}</td>
                  <td>{aIE.tenantID}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/aie/${aIE.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/aie/${aIE.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/aie/${aIE.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="reactSampleApp.aIE.home.notFound">No AIES found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={aIEList && aIEList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default AIE;
