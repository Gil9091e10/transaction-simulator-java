import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMessageIsoConfig, NewMessageIsoConfig } from '../message-iso-config.model';

export type PartialUpdateMessageIsoConfig = Partial<IMessageIsoConfig> & Pick<IMessageIsoConfig, 'id'>;

export type EntityResponseType = HttpResponse<IMessageIsoConfig>;
export type EntityArrayResponseType = HttpResponse<IMessageIsoConfig[]>;

@Injectable({ providedIn: 'root' })
export class MessageIsoConfigService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/message-iso-configs');

  create(messageIsoConfig: NewMessageIsoConfig): Observable<EntityResponseType> {
    return this.http.post<IMessageIsoConfig>(this.resourceUrl, messageIsoConfig, { observe: 'response' });
  }

  update(messageIsoConfig: IMessageIsoConfig): Observable<EntityResponseType> {
    return this.http.put<IMessageIsoConfig>(
      `${this.resourceUrl}/${this.getMessageIsoConfigIdentifier(messageIsoConfig)}`,
      messageIsoConfig,
      { observe: 'response' },
    );
  }

  partialUpdate(messageIsoConfig: PartialUpdateMessageIsoConfig): Observable<EntityResponseType> {
    return this.http.patch<IMessageIsoConfig>(
      `${this.resourceUrl}/${this.getMessageIsoConfigIdentifier(messageIsoConfig)}`,
      messageIsoConfig,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMessageIsoConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMessageIsoConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMessageIsoConfigIdentifier(messageIsoConfig: Pick<IMessageIsoConfig, 'id'>): number {
    return messageIsoConfig.id;
  }

  compareMessageIsoConfig(o1: Pick<IMessageIsoConfig, 'id'> | null, o2: Pick<IMessageIsoConfig, 'id'> | null): boolean {
    return o1 && o2 ? this.getMessageIsoConfigIdentifier(o1) === this.getMessageIsoConfigIdentifier(o2) : o1 === o2;
  }

  addMessageIsoConfigToCollectionIfMissing<Type extends Pick<IMessageIsoConfig, 'id'>>(
    messageIsoConfigCollection: Type[],
    ...messageIsoConfigsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const messageIsoConfigs: Type[] = messageIsoConfigsToCheck.filter(isPresent);
    if (messageIsoConfigs.length > 0) {
      const messageIsoConfigCollectionIdentifiers = messageIsoConfigCollection.map(messageIsoConfigItem =>
        this.getMessageIsoConfigIdentifier(messageIsoConfigItem),
      );
      const messageIsoConfigsToAdd = messageIsoConfigs.filter(messageIsoConfigItem => {
        const messageIsoConfigIdentifier = this.getMessageIsoConfigIdentifier(messageIsoConfigItem);
        if (messageIsoConfigCollectionIdentifiers.includes(messageIsoConfigIdentifier)) {
          return false;
        }
        messageIsoConfigCollectionIdentifiers.push(messageIsoConfigIdentifier);
        return true;
      });
      return [...messageIsoConfigsToAdd, ...messageIsoConfigCollection];
    }
    return messageIsoConfigCollection;
  }
}
