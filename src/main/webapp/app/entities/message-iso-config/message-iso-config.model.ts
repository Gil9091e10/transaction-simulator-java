import { IAcquirer } from 'app/entities/acquirer/acquirer.model';
import { IMessageTypeIndicator } from 'app/entities/message-type-indicator/message-type-indicator.model';

export interface IMessageIsoConfig {
  id: number;
  name?: string | null;
  filename?: string | null;
  acquirer?: Pick<IAcquirer, 'id'> | null;
  messageTypeIndicator?: Pick<IMessageTypeIndicator, 'id'> | null;
}

export type NewMessageIsoConfig = Omit<IMessageIsoConfig, 'id'> & { id: null };
