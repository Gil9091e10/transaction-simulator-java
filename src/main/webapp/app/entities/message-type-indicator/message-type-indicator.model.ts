export interface IMessageTypeIndicator {
  id: number;
  name?: string | null;
  code?: string | null;
}

export type NewMessageTypeIndicator = Omit<IMessageTypeIndicator, 'id'> & { id: null };
