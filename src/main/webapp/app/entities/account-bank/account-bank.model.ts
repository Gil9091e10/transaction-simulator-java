import { ICurrency } from 'app/entities/currency/currency.model';

export interface IAccountBank {
  id: number;
  number?: number | null;
  numberIBAN?: string | null;
  balance?: number | null;
  currency?: Pick<ICurrency, 'id'> | null;
}

export type NewAccountBank = Omit<IAccountBank, 'id'> & { id: null };
