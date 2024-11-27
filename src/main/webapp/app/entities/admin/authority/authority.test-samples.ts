import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'f7d3844d-19e2-4f4d-848f-7bf7880069b3',
};

export const sampleWithPartialData: IAuthority = {
  name: '37f3e0b1-fcf7-418b-86cc-eb6ab1dae6e7',
};

export const sampleWithFullData: IAuthority = {
  name: 'a5b652ce-7003-4605-b9e0-1a77ecdb3996',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
