import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Applicant from './applicant';
import ApplicantDetail from './applicant-detail';
import ApplicantUpdate from './applicant-update';
import ApplicantDeleteDialog from './applicant-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ApplicantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ApplicantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ApplicantDetail} />
      <ErrorBoundaryRoute path={match.url} component={Applicant} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ApplicantDeleteDialog} />
  </>
);

export default Routes;
