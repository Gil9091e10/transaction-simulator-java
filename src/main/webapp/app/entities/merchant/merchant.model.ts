export interface IMerchant {
  id: number;
  name?: string | null;
  mcc?: string | null;
  postalCode?: number | null;
  website?: string | null;
}

export type NewMerchant = Omit<IMerchant, 'id'> & { id: null };
