export interface ITransactionType {
  id: number;
  code?: string | null;
  name?: string | null;
}

export type NewTransactionType = Omit<ITransactionType, 'id'> & { id: null };
