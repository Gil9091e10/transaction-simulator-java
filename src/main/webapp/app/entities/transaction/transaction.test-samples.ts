import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: 8304,
  date: dayjs('2024-11-26T16:29'),
  message: 'until',
};

export const sampleWithPartialData: ITransaction = {
  id: 292,
  date: dayjs('2024-11-26T15:37'),
  message: 'via',
};

export const sampleWithFullData: ITransaction = {
  id: 27439,
  date: dayjs('2024-11-26T20:19'),
  message: 'um truly a',
};

export const sampleWithNewData: NewTransaction = {
  date: dayjs('2024-11-26T14:04'),
  message: 'softly lest joyfully',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
