import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IApplicant } from 'app/shared/model/applicant.model';
import { getEntities as getApplicants } from 'app/entities/applicant/applicant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './quest.reducer';
import { IQuest } from 'app/shared/model/quest.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { QuestStatus } from 'app/shared/model/enumerations/quest-status.model';

export const QuestUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const applicants = useAppSelector(state => state.applicant.entities);
  const questEntity = useAppSelector(state => state.quest.entity);
  const loading = useAppSelector(state => state.quest.loading);
  const updating = useAppSelector(state => state.quest.updating);
  const updateSuccess = useAppSelector(state => state.quest.updateSuccess);
  const questStatusValues = Object.keys(QuestStatus);
  const handleClose = () => {
    props.history.push('/quest' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getApplicants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...questEntity,
      ...values,
      applicants: mapIdList(values.applicants),
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
          status: 'OPEN',
          ...questEntity,
          applicants: questEntity?.applicants?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="subsquidQuestManagerApp.quest.home.createOrEditLabel" data-cy="QuestCreateUpdateHeading">
            Create or edit a Quest
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="quest-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Title"
                id="quest-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="quest-description" name="description" data-cy="description" type="text" />
              <ValidatedField
                label="Reward"
                id="quest-reward"
                name="reward"
                data-cy="reward"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Expires On"
                id="quest-expiresOn"
                name="expiresOn"
                data-cy="expiresOn"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Review Start Date"
                id="quest-reviewStartDate"
                name="reviewStartDate"
                data-cy="reviewStartDate"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Max Applicants"
                id="quest-maxApplicants"
                name="maxApplicants"
                data-cy="maxApplicants"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  min: { value: 1, message: 'This field should be at least 1.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Assignee" id="quest-assignee" name="assignee" data-cy="assignee" type="text" />
              <ValidatedField label="Status" id="quest-status" name="status" data-cy="status" type="select">
                {questStatusValues.map(questStatus => (
                  <option value={questStatus} key={questStatus}>
                    {questStatus}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Private Notes" id="quest-privateNotes" name="privateNotes" data-cy="privateNotes" type="text" />
              <ValidatedField label="Applicant" id="quest-applicant" data-cy="applicant" type="select" multiple name="applicants">
                <option value="" key="0" />
                {applicants
                  ? applicants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.discordHandle}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/quest" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default QuestUpdate;
