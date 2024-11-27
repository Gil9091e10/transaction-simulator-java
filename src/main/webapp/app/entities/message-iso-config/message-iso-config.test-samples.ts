import { IMessageIsoConfig, NewMessageIsoConfig } from './message-iso-config.model';

export const sampleWithRequiredData: IMessageIsoConfig = {
  id: 17711,
  name: 'despite beside terribly',
  filename: 'thick',
};

export const sampleWithPartialData: IMessageIsoConfig = {
  id: 29163,
  name: 'wordy as',
  filename: 'hyena aha',
};

export const sampleWithFullData: IMessageIsoConfig = {
  id: 24639,
  name: 'glaring',
  filename: 'unbearably',
};

export const sampleWithNewData: NewMessageIsoConfig = {
  name: 'uh-huh obtrude meanwhile',
  filename: 'yet helplessly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
