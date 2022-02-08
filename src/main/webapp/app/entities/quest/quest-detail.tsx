import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './quest.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const QuestDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const questEntity = useAppSelector(state => state.quest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questDetailsHeading">Quest</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{questEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{questEntity.title}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{questEntity.description}</dd>
          <dt>
            <span id="reward">Reward</span>
          </dt>
          <dd>{questEntity.reward}</dd>
          <dt>
            <span id="expiresOn">Expires On</span>
          </dt>
          <dd>{questEntity.expiresOn ? <TextFormat value={questEntity.expiresOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="reviewStartDate">Review Start Date</span>
          </dt>
          <dd>
            {questEntity.reviewStartDate ? (
              <TextFormat value={questEntity.reviewStartDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="maxApplicants">Max Applicants</span>
          </dt>
          <dd>{questEntity.maxApplicants}</dd>
          <dt>
            <span id="assignee">Assignee</span>
          </dt>
          <dd>{questEntity.assignee}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{questEntity.status}</dd>
          <dt>
            <span id="privateNotes">Private Notes</span>
          </dt>
          <dd>{questEntity.privateNotes}</dd>
        </dl>
        <Button tag={Link} to="/quest" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/quest/${questEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestDetail;
