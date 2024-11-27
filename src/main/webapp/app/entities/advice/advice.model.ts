import { IMerchant } from 'app/entities/merchant/merchant.model';
import { IAcquirer } from 'app/entities/acquirer/acquirer.model';

export interface IAdvice {
  id: number;
  code?: string | null;
  name?: string | null;
  merchant?: Pick<IMerchant, 'id'> | null;
  acquirer?: Pick<IAcquirer, 'id'> | null;
}

export type NewAdvice = Omit<IAdvice, 'id'> & { id: null };
