import { IMessageTypeIndicator, NewMessageTypeIndicator } from './message-type-indicator.model';

export const sampleWithRequiredData: IMessageTypeIndicator = {
  id: 3246,
  name: 'fatally',
  code: 'jaun',
};

export const sampleWithPartialData: IMessageTypeIndicator = {
  id: 26863,
  name: 'selfishly yuck though',
  code: 'thou',
};

export const sampleWithFullData: IMessageTypeIndicator = {
  id: 10381,
  name: 'roughly quicker',
  code: 'sadl',
};

export const sampleWithNewData: NewMessageTypeIndicator = {
  name: 'anti whoa atop',
  code: 'booh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
