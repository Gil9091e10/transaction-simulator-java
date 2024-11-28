import dayjs from 'dayjs';
import { ITransactionType } from 'app/shared/model/transaction-type.model';

export interface ITransaction {
  id?: number;
  date?: dayjs.Dayjs;
  message?: string;
  transactionType?: ITransactionType | null;
}

export const defaultValue: Readonly<ITransaction> = {};
