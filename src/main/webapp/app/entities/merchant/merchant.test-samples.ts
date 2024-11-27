import { IMerchant, NewMerchant } from './merchant.model';

export const sampleWithRequiredData: IMerchant = {
  id: 25109,
  name: 'overheard fervently officially',
  mcc: 'anti ferociously pish',
};

export const sampleWithPartialData: IMerchant = {
  id: 3270,
  name: 'corral ha',
  mcc: 'flowery repeatedly apropos',
  postalCode: 23479,
  website: 'finally adviser ugh',
};

export const sampleWithFullData: IMerchant = {
  id: 7497,
  name: 'cluttered pluck gah',
  mcc: 'brandish',
  postalCode: 3004,
  website: 'of',
};

export const sampleWithNewData: NewMerchant = {
  name: 'although pish yowza',
  mcc: 'winged inasmuch',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
