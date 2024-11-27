export interface ICardType {
  id: number;
  name?: string | null;
}

export type NewCardType = Omit<ICardType, 'id'> & { id: null };
