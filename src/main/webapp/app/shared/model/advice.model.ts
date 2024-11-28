import { IMerchant } from 'app/shared/model/merchant.model';
import { IAcquirer } from 'app/shared/model/acquirer.model';

export interface IAdvice {
  id?: number;
  code?: string;
  name?: string;
  merchant?: IMerchant | null;
  acquirer?: IAcquirer | null;
}

export const defaultValue: Readonly<IAdvice> = {};
