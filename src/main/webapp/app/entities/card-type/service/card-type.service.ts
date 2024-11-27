import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICardType, NewCardType } from '../card-type.model';

export type PartialUpdateCardType = Partial<ICardType> & Pick<ICardType, 'id'>;

export type EntityResponseType = HttpResponse<ICardType>;
export type EntityArrayResponseType = HttpResponse<ICardType[]>;

@Injectable({ providedIn: 'root' })
export class CardTypeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-types');

  create(cardType: NewCardType): Observable<EntityResponseType> {
    return this.http.post<ICardType>(this.resourceUrl, cardType, { observe: 'response' });
  }

  update(cardType: ICardType): Observable<EntityResponseType> {
    return this.http.put<ICardType>(`${this.resourceUrl}/${this.getCardTypeIdentifier(cardType)}`, cardType, { observe: 'response' });
  }

  partialUpdate(cardType: PartialUpdateCardType): Observable<EntityResponseType> {
    return this.http.patch<ICardType>(`${this.resourceUrl}/${this.getCardTypeIdentifier(cardType)}`, cardType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICardType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCardTypeIdentifier(cardType: Pick<ICardType, 'id'>): number {
    return cardType.id;
  }

  compareCardType(o1: Pick<ICardType, 'id'> | null, o2: Pick<ICardType, 'id'> | null): boolean {
    return o1 && o2 ? this.getCardTypeIdentifier(o1) === this.getCardTypeIdentifier(o2) : o1 === o2;
  }

  addCardTypeToCollectionIfMissing<Type extends Pick<ICardType, 'id'>>(
    cardTypeCollection: Type[],
    ...cardTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cardTypes: Type[] = cardTypesToCheck.filter(isPresent);
    if (cardTypes.length > 0) {
      const cardTypeCollectionIdentifiers = cardTypeCollection.map(cardTypeItem => this.getCardTypeIdentifier(cardTypeItem));
      const cardTypesToAdd = cardTypes.filter(cardTypeItem => {
        const cardTypeIdentifier = this.getCardTypeIdentifier(cardTypeItem);
        if (cardTypeCollectionIdentifiers.includes(cardTypeIdentifier)) {
          return false;
        }
        cardTypeCollectionIdentifiers.push(cardTypeIdentifier);
        return true;
      });
      return [...cardTypesToAdd, ...cardTypeCollection];
    }
    return cardTypeCollection;
  }
}
