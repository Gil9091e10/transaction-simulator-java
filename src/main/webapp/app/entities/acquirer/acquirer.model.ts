export interface IAcquirer {
  id: number;
  name?: string | null;
  socketUrl?: string | null;
  email?: string | null;
}

export type NewAcquirer = Omit<IAcquirer, 'id'> & { id: null };
