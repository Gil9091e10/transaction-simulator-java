import dayjs from 'dayjs';
import { IAccountBank } from 'app/shared/model/account-bank.model';
import { ICardType } from 'app/shared/model/card-type.model';
import { IIssuer } from 'app/shared/model/issuer.model';

export interface ICard {
  id?: number;
  number?: number;
  expirationDate?: dayjs.Dayjs | null;
  verificationValue?: number | null;
  accountBank?: IAccountBank | null;
  cardType?: ICardType | null;
  issuer?: IIssuer | null;
}

export const defaultValue: Readonly<ICard> = {};
