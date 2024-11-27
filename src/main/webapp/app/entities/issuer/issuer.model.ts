export interface IIssuer {
  id: number;
  code?: string | null;
  name?: string | null;
}

export type NewIssuer = Omit<IIssuer, 'id'> & { id: null };
