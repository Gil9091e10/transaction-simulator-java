import { ICardType, NewCardType } from './card-type.model';

export const sampleWithRequiredData: ICardType = {
  id: 26141,
  name: 'bicycle although sociable',
};

export const sampleWithPartialData: ICardType = {
  id: 24496,
  name: 'unless',
};

export const sampleWithFullData: ICardType = {
  id: 2704,
  name: 'dishearten',
};

export const sampleWithNewData: NewCardType = {
  name: 'but uncommon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
