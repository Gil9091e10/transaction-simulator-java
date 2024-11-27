import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMessageTypeIndicator, NewMessageTypeIndicator } from '../message-type-indicator.model';

export type PartialUpdateMessageTypeIndicator = Partial<IMessageTypeIndicator> & Pick<IMessageTypeIndicator, 'id'>;

export type EntityResponseType = HttpResponse<IMessageTypeIndicator>;
export type EntityArrayResponseType = HttpResponse<IMessageTypeIndicator[]>;

@Injectable({ providedIn: 'root' })
export class MessageTypeIndicatorService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/message-type-indicators');

  create(messageTypeIndicator: NewMessageTypeIndicator): Observable<EntityResponseType> {
    return this.http.post<IMessageTypeIndicator>(this.resourceUrl, messageTypeIndicator, { observe: 'response' });
  }

  update(messageTypeIndicator: IMessageTypeIndicator): Observable<EntityResponseType> {
    return this.http.put<IMessageTypeIndicator>(
      `${this.resourceUrl}/${this.getMessageTypeIndicatorIdentifier(messageTypeIndicator)}`,
      messageTypeIndicator,
      { observe: 'response' },
    );
  }

  partialUpdate(messageTypeIndicator: PartialUpdateMessageTypeIndicator): Observable<EntityResponseType> {
    return this.http.patch<IMessageTypeIndicator>(
      `${this.resourceUrl}/${this.getMessageTypeIndicatorIdentifier(messageTypeIndicator)}`,
      messageTypeIndicator,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMessageTypeIndicator>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMessageTypeIndicator[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMessageTypeIndicatorIdentifier(messageTypeIndicator: Pick<IMessageTypeIndicator, 'id'>): number {
    return messageTypeIndicator.id;
  }

  compareMessageTypeIndicator(o1: Pick<IMessageTypeIndicator, 'id'> | null, o2: Pick<IMessageTypeIndicator, 'id'> | null): boolean {
    return o1 && o2 ? this.getMessageTypeIndicatorIdentifier(o1) === this.getMessageTypeIndicatorIdentifier(o2) : o1 === o2;
  }

  addMessageTypeIndicatorToCollectionIfMissing<Type extends Pick<IMessageTypeIndicator, 'id'>>(
    messageTypeIndicatorCollection: Type[],
    ...messageTypeIndicatorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const messageTypeIndicators: Type[] = messageTypeIndicatorsToCheck.filter(isPresent);
    if (messageTypeIndicators.length > 0) {
      const messageTypeIndicatorCollectionIdentifiers = messageTypeIndicatorCollection.map(messageTypeIndicatorItem =>
        this.getMessageTypeIndicatorIdentifier(messageTypeIndicatorItem),
      );
      const messageTypeIndicatorsToAdd = messageTypeIndicators.filter(messageTypeIndicatorItem => {
        const messageTypeIndicatorIdentifier = this.getMessageTypeIndicatorIdentifier(messageTypeIndicatorItem);
        if (messageTypeIndicatorCollectionIdentifiers.includes(messageTypeIndicatorIdentifier)) {
          return false;
        }
        messageTypeIndicatorCollectionIdentifiers.push(messageTypeIndicatorIdentifier);
        return true;
      });
      return [...messageTypeIndicatorsToAdd, ...messageTypeIndicatorCollection];
    }
    return messageTypeIndicatorCollection;
  }
}
