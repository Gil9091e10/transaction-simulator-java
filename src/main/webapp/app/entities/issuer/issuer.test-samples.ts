import { IIssuer, NewIssuer } from './issuer.model';

export const sampleWithRequiredData: IIssuer = {
  id: 30396,
  code: 'deliberate',
  name: 'brilliant',
};

export const sampleWithPartialData: IIssuer = {
  id: 4947,
  code: 'bulky',
  name: 'awkwardly untrue fishery',
};

export const sampleWithFullData: IIssuer = {
  id: 19474,
  code: 'whoa furth',
  name: 'yearly',
};

export const sampleWithNewData: NewIssuer = {
  code: 'gracefully',
  name: 'purse whoa',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
