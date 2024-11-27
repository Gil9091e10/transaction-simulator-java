import { IAcquirer, NewAcquirer } from './acquirer.model';

export const sampleWithRequiredData: IAcquirer = {
  id: 8236,
  name: 'slow per',
  socketUrl: 'fortunate',
  email: 'Ramona.PulidoArteaga@yahoo.com',
};

export const sampleWithPartialData: IAcquirer = {
  id: 28251,
  name: 'merrily drum never',
  socketUrl: 'eventually dreary vamoose',
  email: 'JoseEmilio39@yahoo.com',
};

export const sampleWithFullData: IAcquirer = {
  id: 28035,
  name: 'milky unless',
  socketUrl: 'unethically reboot',
  email: 'Mercedes9@hotmail.com',
};

export const sampleWithNewData: NewAcquirer = {
  name: 'even clear',
  socketUrl: 'nephew',
  email: 'Javier.NevarezLucio@hotmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
