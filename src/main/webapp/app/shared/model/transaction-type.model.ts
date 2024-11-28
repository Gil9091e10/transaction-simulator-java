export interface ITransactionType {
  id?: number;
  code?: string;
  name?: string;
}

export const defaultValue: Readonly<ITransactionType> = {};
