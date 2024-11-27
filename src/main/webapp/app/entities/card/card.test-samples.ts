import dayjs from 'dayjs/esm';

import { ICard, NewCard } from './card.model';

export const sampleWithRequiredData: ICard = {
  id: 30527,
  number: 21844,
};

export const sampleWithPartialData: ICard = {
  id: 15454,
  number: 28593,
  expirationDate: dayjs('2024-11-26'),
  verificationValue: 18111,
};

export const sampleWithFullData: ICard = {
  id: 24747,
  number: 18931,
  expirationDate: dayjs('2024-11-26'),
  verificationValue: 8172,
};

export const sampleWithNewData: NewCard = {
  number: 11416,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
