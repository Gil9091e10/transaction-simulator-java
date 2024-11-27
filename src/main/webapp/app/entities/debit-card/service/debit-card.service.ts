import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDebitCard, NewDebitCard } from '../debit-card.model';

export type EntityResponseType = HttpResponse<IDebitCard>;
export type EntityArrayResponseType = HttpResponse<IDebitCard[]>;

@Injectable({ providedIn: 'root' })
export class DebitCardService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/debit-cards');

  create(debitCard: NewDebitCard): Observable<EntityResponseType> {
    return this.http.post<IDebitCard>(this.resourceUrl, debitCard, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDebitCard>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDebitCard[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDebitCardIdentifier(debitCard: Pick<IDebitCard, 'id'>): number {
    return debitCard.id;
  }

  compareDebitCard(o1: Pick<IDebitCard, 'id'> | null, o2: Pick<IDebitCard, 'id'> | null): boolean {
    return o1 && o2 ? this.getDebitCardIdentifier(o1) === this.getDebitCardIdentifier(o2) : o1 === o2;
  }

  addDebitCardToCollectionIfMissing<Type extends Pick<IDebitCard, 'id'>>(
    debitCardCollection: Type[],
    ...debitCardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const debitCards: Type[] = debitCardsToCheck.filter(isPresent);
    if (debitCards.length > 0) {
      const debitCardCollectionIdentifiers = debitCardCollection.map(debitCardItem => this.getDebitCardIdentifier(debitCardItem));
      const debitCardsToAdd = debitCards.filter(debitCardItem => {
        const debitCardIdentifier = this.getDebitCardIdentifier(debitCardItem);
        if (debitCardCollectionIdentifiers.includes(debitCardIdentifier)) {
          return false;
        }
        debitCardCollectionIdentifiers.push(debitCardIdentifier);
        return true;
      });
      return [...debitCardsToAdd, ...debitCardCollection];
    }
    return debitCardCollection;
  }
}
