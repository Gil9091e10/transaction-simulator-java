import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdvice, NewAdvice } from '../advice.model';

export type PartialUpdateAdvice = Partial<IAdvice> & Pick<IAdvice, 'id'>;

export type EntityResponseType = HttpResponse<IAdvice>;
export type EntityArrayResponseType = HttpResponse<IAdvice[]>;

@Injectable({ providedIn: 'root' })
export class AdviceService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/advice');

  create(advice: NewAdvice): Observable<EntityResponseType> {
    return this.http.post<IAdvice>(this.resourceUrl, advice, { observe: 'response' });
  }

  update(advice: IAdvice): Observable<EntityResponseType> {
    return this.http.put<IAdvice>(`${this.resourceUrl}/${this.getAdviceIdentifier(advice)}`, advice, { observe: 'response' });
  }

  partialUpdate(advice: PartialUpdateAdvice): Observable<EntityResponseType> {
    return this.http.patch<IAdvice>(`${this.resourceUrl}/${this.getAdviceIdentifier(advice)}`, advice, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdvice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdvice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAdviceIdentifier(advice: Pick<IAdvice, 'id'>): number {
    return advice.id;
  }

  compareAdvice(o1: Pick<IAdvice, 'id'> | null, o2: Pick<IAdvice, 'id'> | null): boolean {
    return o1 && o2 ? this.getAdviceIdentifier(o1) === this.getAdviceIdentifier(o2) : o1 === o2;
  }

  addAdviceToCollectionIfMissing<Type extends Pick<IAdvice, 'id'>>(
    adviceCollection: Type[],
    ...adviceToCheck: (Type | null | undefined)[]
  ): Type[] {
    const advice: Type[] = adviceToCheck.filter(isPresent);
    if (advice.length > 0) {
      const adviceCollectionIdentifiers = adviceCollection.map(adviceItem => this.getAdviceIdentifier(adviceItem));
      const adviceToAdd = advice.filter(adviceItem => {
        const adviceIdentifier = this.getAdviceIdentifier(adviceItem);
        if (adviceCollectionIdentifiers.includes(adviceIdentifier)) {
          return false;
        }
        adviceCollectionIdentifiers.push(adviceIdentifier);
        return true;
      });
      return [...adviceToAdd, ...adviceCollection];
    }
    return adviceCollection;
  }
}
