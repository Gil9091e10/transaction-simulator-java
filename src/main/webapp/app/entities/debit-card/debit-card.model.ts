export interface IDebitCard {
  id: number;
}

export type NewDebitCard = Omit<IDebitCard, 'id'> & { id: null };
