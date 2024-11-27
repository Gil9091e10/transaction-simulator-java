export interface ICreditCard {
  id: number;
  maxLimit?: number | null;
}

export type NewCreditCard = Omit<ICreditCard, 'id'> & { id: null };
