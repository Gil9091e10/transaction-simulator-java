export interface IMerchant {
  id?: number;
  name?: string;
  mcc?: string;
  postalCode?: number | null;
  website?: string | null;
}

export const defaultValue: Readonly<IMerchant> = {};
