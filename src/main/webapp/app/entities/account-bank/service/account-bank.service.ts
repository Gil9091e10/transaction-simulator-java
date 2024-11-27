import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccountBank, NewAccountBank } from '../account-bank.model';

export type PartialUpdateAccountBank = Partial<IAccountBank> & Pick<IAccountBank, 'id'>;

export type EntityResponseType = HttpResponse<IAccountBank>;
export type EntityArrayResponseType = HttpResponse<IAccountBank[]>;

@Injectable({ providedIn: 'root' })
export class AccountBankService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-banks');

  create(accountBank: NewAccountBank): Observable<EntityResponseType> {
    return this.http.post<IAccountBank>(this.resourceUrl, accountBank, { observe: 'response' });
  }

  update(accountBank: IAccountBank): Observable<EntityResponseType> {
    return this.http.put<IAccountBank>(`${this.resourceUrl}/${this.getAccountBankIdentifier(accountBank)}`, accountBank, {
      observe: 'response',
    });
  }

  partialUpdate(accountBank: PartialUpdateAccountBank): Observable<EntityResponseType> {
    return this.http.patch<IAccountBank>(`${this.resourceUrl}/${this.getAccountBankIdentifier(accountBank)}`, accountBank, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccountBank>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountBank[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAccountBankIdentifier(accountBank: Pick<IAccountBank, 'id'>): number {
    return accountBank.id;
  }

  compareAccountBank(o1: Pick<IAccountBank, 'id'> | null, o2: Pick<IAccountBank, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccountBankIdentifier(o1) === this.getAccountBankIdentifier(o2) : o1 === o2;
  }

  addAccountBankToCollectionIfMissing<Type extends Pick<IAccountBank, 'id'>>(
    accountBankCollection: Type[],
    ...accountBanksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accountBanks: Type[] = accountBanksToCheck.filter(isPresent);
    if (accountBanks.length > 0) {
      const accountBankCollectionIdentifiers = accountBankCollection.map(accountBankItem => this.getAccountBankIdentifier(accountBankItem));
      const accountBanksToAdd = accountBanks.filter(accountBankItem => {
        const accountBankIdentifier = this.getAccountBankIdentifier(accountBankItem);
        if (accountBankCollectionIdentifiers.includes(accountBankIdentifier)) {
          return false;
        }
        accountBankCollectionIdentifiers.push(accountBankIdentifier);
        return true;
      });
      return [...accountBanksToAdd, ...accountBankCollection];
    }
    return accountBankCollection;
  }
}
