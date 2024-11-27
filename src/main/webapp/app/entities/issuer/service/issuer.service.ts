import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIssuer, NewIssuer } from '../issuer.model';

export type PartialUpdateIssuer = Partial<IIssuer> & Pick<IIssuer, 'id'>;

export type EntityResponseType = HttpResponse<IIssuer>;
export type EntityArrayResponseType = HttpResponse<IIssuer[]>;

@Injectable({ providedIn: 'root' })
export class IssuerService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/issuers');

  create(issuer: NewIssuer): Observable<EntityResponseType> {
    return this.http.post<IIssuer>(this.resourceUrl, issuer, { observe: 'response' });
  }

  update(issuer: IIssuer): Observable<EntityResponseType> {
    return this.http.put<IIssuer>(`${this.resourceUrl}/${this.getIssuerIdentifier(issuer)}`, issuer, { observe: 'response' });
  }

  partialUpdate(issuer: PartialUpdateIssuer): Observable<EntityResponseType> {
    return this.http.patch<IIssuer>(`${this.resourceUrl}/${this.getIssuerIdentifier(issuer)}`, issuer, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIssuer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIssuer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIssuerIdentifier(issuer: Pick<IIssuer, 'id'>): number {
    return issuer.id;
  }

  compareIssuer(o1: Pick<IIssuer, 'id'> | null, o2: Pick<IIssuer, 'id'> | null): boolean {
    return o1 && o2 ? this.getIssuerIdentifier(o1) === this.getIssuerIdentifier(o2) : o1 === o2;
  }

  addIssuerToCollectionIfMissing<Type extends Pick<IIssuer, 'id'>>(
    issuerCollection: Type[],
    ...issuersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const issuers: Type[] = issuersToCheck.filter(isPresent);
    if (issuers.length > 0) {
      const issuerCollectionIdentifiers = issuerCollection.map(issuerItem => this.getIssuerIdentifier(issuerItem));
      const issuersToAdd = issuers.filter(issuerItem => {
        const issuerIdentifier = this.getIssuerIdentifier(issuerItem);
        if (issuerCollectionIdentifiers.includes(issuerIdentifier)) {
          return false;
        }
        issuerCollectionIdentifiers.push(issuerIdentifier);
        return true;
      });
      return [...issuersToAdd, ...issuerCollection];
    }
    return issuerCollection;
  }
}
