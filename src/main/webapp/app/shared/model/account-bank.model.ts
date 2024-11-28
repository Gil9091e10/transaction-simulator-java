import { ICurrency } from 'app/shared/model/currency.model';

export interface IAccountBank {
  id?: number;
  number?: number;
  numberIBAN?: string;
  balance?: number;
  currency?: ICurrency | null;
}

export const defaultValue: Readonly<IAccountBank> = {};
