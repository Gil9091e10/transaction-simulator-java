import { IAdvice, NewAdvice } from './advice.model';

export const sampleWithRequiredData: IAdvice = {
  id: 6518,
  code: 'chap those',
  name: 'delirious that instead',
};

export const sampleWithPartialData: IAdvice = {
  id: 11018,
  code: 'really opp',
  name: 'atop oh',
};

export const sampleWithFullData: IAdvice = {
  id: 8353,
  code: 'matter',
  name: 'for brr natural',
};

export const sampleWithNewData: NewAdvice = {
  code: 'with',
  name: 'failing',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
