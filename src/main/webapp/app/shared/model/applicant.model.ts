import { IQuest } from 'app/shared/model/quest.model';

export interface IApplicant {
  id?: number;
  discordHandle?: string;
  quests?: IQuest[] | null;
}

export const defaultValue: Readonly<IApplicant> = {};
