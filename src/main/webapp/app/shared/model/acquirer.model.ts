export interface IAcquirer {
  id?: number;
  name?: string;
  socketUrl?: string;
  email?: string;
}

export const defaultValue: Readonly<IAcquirer> = {};
