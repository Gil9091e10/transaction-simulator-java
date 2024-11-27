import { ICurrency, NewCurrency } from './currency.model';

export const sampleWithRequiredData: ICurrency = {
  id: 18865,
  name: 'since prem',
  code: 'often',
};

export const sampleWithPartialData: ICurrency = {
  id: 14198,
  name: 'recklessly',
  code: 'knowl',
};

export const sampleWithFullData: ICurrency = {
  id: 4961,
  name: 'packaging ',
  code: 'now b',
};

export const sampleWithNewData: NewCurrency = {
  name: 'huzzah',
  code: 'viole',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
