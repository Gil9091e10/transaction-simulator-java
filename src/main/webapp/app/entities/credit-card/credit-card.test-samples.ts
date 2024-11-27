import { ICreditCard, NewCreditCard } from './credit-card.model';

export const sampleWithRequiredData: ICreditCard = {
  id: 21626,
  maxLimit: 18751.23,
};

export const sampleWithPartialData: ICreditCard = {
  id: 20438,
  maxLimit: 12346.04,
};

export const sampleWithFullData: ICreditCard = {
  id: 6714,
  maxLimit: 8064.97,
};

export const sampleWithNewData: NewCreditCard = {
  maxLimit: 11526.49,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
