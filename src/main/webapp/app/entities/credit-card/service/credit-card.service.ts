import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICreditCard, NewCreditCard } from '../credit-card.model';

export type PartialUpdateCreditCard = Partial<ICreditCard> & Pick<ICreditCard, 'id'>;

export type EntityResponseType = HttpResponse<ICreditCard>;
export type EntityArrayResponseType = HttpResponse<ICreditCard[]>;

@Injectable({ providedIn: 'root' })
export class CreditCardService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/credit-cards');

  create(creditCard: NewCreditCard): Observable<EntityResponseType> {
    return this.http.post<ICreditCard>(this.resourceUrl, creditCard, { observe: 'response' });
  }

  update(creditCard: ICreditCard): Observable<EntityResponseType> {
    return this.http.put<ICreditCard>(`${this.resourceUrl}/${this.getCreditCardIdentifier(creditCard)}`, creditCard, {
      observe: 'response',
    });
  }

  partialUpdate(creditCard: PartialUpdateCreditCard): Observable<EntityResponseType> {
    return this.http.patch<ICreditCard>(`${this.resourceUrl}/${this.getCreditCardIdentifier(creditCard)}`, creditCard, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICreditCard>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICreditCard[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCreditCardIdentifier(creditCard: Pick<ICreditCard, 'id'>): number {
    return creditCard.id;
  }

  compareCreditCard(o1: Pick<ICreditCard, 'id'> | null, o2: Pick<ICreditCard, 'id'> | null): boolean {
    return o1 && o2 ? this.getCreditCardIdentifier(o1) === this.getCreditCardIdentifier(o2) : o1 === o2;
  }

  addCreditCardToCollectionIfMissing<Type extends Pick<ICreditCard, 'id'>>(
    creditCardCollection: Type[],
    ...creditCardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const creditCards: Type[] = creditCardsToCheck.filter(isPresent);
    if (creditCards.length > 0) {
      const creditCardCollectionIdentifiers = creditCardCollection.map(creditCardItem => this.getCreditCardIdentifier(creditCardItem));
      const creditCardsToAdd = creditCards.filter(creditCardItem => {
        const creditCardIdentifier = this.getCreditCardIdentifier(creditCardItem);
        if (creditCardCollectionIdentifiers.includes(creditCardIdentifier)) {
          return false;
        }
        creditCardCollectionIdentifiers.push(creditCardIdentifier);
        return true;
      });
      return [...creditCardsToAdd, ...creditCardCollection];
    }
    return creditCardCollection;
  }
}
