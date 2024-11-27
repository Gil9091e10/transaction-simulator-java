import { IAccountBank, NewAccountBank } from './account-bank.model';

export const sampleWithRequiredData: IAccountBank = {
  id: 13780,
  number: 370,
  numberIBAN: 'husband',
  balance: 23117.04,
};

export const sampleWithPartialData: IAccountBank = {
  id: 29821,
  number: 26971,
  numberIBAN: 'midst epic pleasant',
  balance: 3518.48,
};

export const sampleWithFullData: IAccountBank = {
  id: 11957,
  number: 24398,
  numberIBAN: 'cow',
  balance: 30639.66,
};

export const sampleWithNewData: NewAccountBank = {
  number: 8353,
  numberIBAN: 'stiff regarding',
  balance: 22792.09,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
