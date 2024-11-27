import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAcquirer, NewAcquirer } from '../acquirer.model';

export type PartialUpdateAcquirer = Partial<IAcquirer> & Pick<IAcquirer, 'id'>;

export type EntityResponseType = HttpResponse<IAcquirer>;
export type EntityArrayResponseType = HttpResponse<IAcquirer[]>;

@Injectable({ providedIn: 'root' })
export class AcquirerService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acquirers');

  create(acquirer: NewAcquirer): Observable<EntityResponseType> {
    return this.http.post<IAcquirer>(this.resourceUrl, acquirer, { observe: 'response' });
  }

  update(acquirer: IAcquirer): Observable<EntityResponseType> {
    return this.http.put<IAcquirer>(`${this.resourceUrl}/${this.getAcquirerIdentifier(acquirer)}`, acquirer, { observe: 'response' });
  }

  partialUpdate(acquirer: PartialUpdateAcquirer): Observable<EntityResponseType> {
    return this.http.patch<IAcquirer>(`${this.resourceUrl}/${this.getAcquirerIdentifier(acquirer)}`, acquirer, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAcquirer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAcquirer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAcquirerIdentifier(acquirer: Pick<IAcquirer, 'id'>): number {
    return acquirer.id;
  }

  compareAcquirer(o1: Pick<IAcquirer, 'id'> | null, o2: Pick<IAcquirer, 'id'> | null): boolean {
    return o1 && o2 ? this.getAcquirerIdentifier(o1) === this.getAcquirerIdentifier(o2) : o1 === o2;
  }

  addAcquirerToCollectionIfMissing<Type extends Pick<IAcquirer, 'id'>>(
    acquirerCollection: Type[],
    ...acquirersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const acquirers: Type[] = acquirersToCheck.filter(isPresent);
    if (acquirers.length > 0) {
      const acquirerCollectionIdentifiers = acquirerCollection.map(acquirerItem => this.getAcquirerIdentifier(acquirerItem));
      const acquirersToAdd = acquirers.filter(acquirerItem => {
        const acquirerIdentifier = this.getAcquirerIdentifier(acquirerItem);
        if (acquirerCollectionIdentifiers.includes(acquirerIdentifier)) {
          return false;
        }
        acquirerCollectionIdentifiers.push(acquirerIdentifier);
        return true;
      });
      return [...acquirersToAdd, ...acquirerCollection];
    }
    return acquirerCollection;
  }
}
