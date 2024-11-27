import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 32131,
  login: 'i6L',
};

export const sampleWithPartialData: IUser = {
  id: 10159,
  login: 'Q^.d@bFIjb\\TR5xB8Q',
};

export const sampleWithFullData: IUser = {
  id: 26090,
  login: 'm&hH@FS4tl8\\s8sYa\\2u5K',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
