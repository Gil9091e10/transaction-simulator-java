import dayjs from 'dayjs/esm';
import { IAccountBank } from 'app/entities/account-bank/account-bank.model';
import { ICardType } from 'app/entities/card-type/card-type.model';
import { IIssuer } from 'app/entities/issuer/issuer.model';

export interface ICard {
  id: number;
  number?: number | null;
  expirationDate?: dayjs.Dayjs | null;
  verificationValue?: number | null;
  accountBank?: Pick<IAccountBank, 'id'> | null;
  cardType?: Pick<ICardType, 'id'> | null;
  issuer?: Pick<IIssuer, 'id'> | null;
}

export type NewCard = Omit<ICard, 'id'> & { id: null };
