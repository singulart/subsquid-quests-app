import dayjs from 'dayjs';
import { IApplicant } from 'app/shared/model/applicant.model';
import { QuestStatus } from 'app/shared/model/enumerations/quest-status.model';

export interface IQuest {
  id?: number;
  title?: string;
  description?: string | null;
  reward?: string;
  expiresOn?: string;
  reviewStartDate?: string;
  maxApplicants?: number;
  assignee?: string | null;
  status?: QuestStatus;
  privateNotes?: string | null;
  applicants?: IApplicant[] | null;
}

export const defaultValue: Readonly<IQuest> = {};
