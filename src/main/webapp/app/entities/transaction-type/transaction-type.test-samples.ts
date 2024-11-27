import { ITransactionType, NewTransactionType } from './transaction-type.model';

export const sampleWithRequiredData: ITransactionType = {
  id: 15084,
  code: 'amid',
  name: 'meh given meanwhile',
};

export const sampleWithPartialData: ITransactionType = {
  id: 31315,
  code: 'pro',
  name: 'even creative',
};

export const sampleWithFullData: ITransactionType = {
  id: 4469,
  code: 'greed',
  name: 'from',
};

export const sampleWithNewData: NewTransactionType = {
  code: 'bah',
  name: 'yum shell condense',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
