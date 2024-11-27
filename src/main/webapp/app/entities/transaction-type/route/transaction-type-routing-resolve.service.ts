import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransactionType } from '../transaction-type.model';
import { TransactionTypeService } from '../service/transaction-type.service';

const transactionTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransactionType> => {
  const id = route.params.id;
  if (id) {
    return inject(TransactionTypeService)
      .find(id)
      .pipe(
        mergeMap((transactionType: HttpResponse<ITransactionType>) => {
          if (transactionType.body) {
            return of(transactionType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default transactionTypeResolve;
