import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Quest from './quest';
import QuestDetail from './quest-detail';
import QuestUpdate from './quest-update';
import QuestDeleteDialog from './quest-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={QuestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={QuestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={QuestDetail} />
      <ErrorBoundaryRoute path={match.url} component={Quest} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={QuestDeleteDialog} />
  </>
);

export default Routes;
