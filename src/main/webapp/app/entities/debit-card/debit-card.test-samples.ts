import { IDebitCard, NewDebitCard } from './debit-card.model';

export const sampleWithRequiredData: IDebitCard = {
  id: 16722,
};

export const sampleWithPartialData: IDebitCard = {
  id: 6219,
};

export const sampleWithFullData: IDebitCard = {
  id: 31483,
};

export const sampleWithNewData: NewDebitCard = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
