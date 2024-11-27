import dayjs from 'dayjs/esm';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';

export interface ITransaction {
  id: number;
  date?: dayjs.Dayjs | null;
  message?: string | null;
  transactionType?: Pick<ITransactionType, 'id'> | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
