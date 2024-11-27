import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDebitCard } from '../debit-card.model';
import { DebitCardService } from '../service/debit-card.service';

const debitCardResolve = (route: ActivatedRouteSnapshot): Observable<null | IDebitCard> => {
  const id = route.params.id;
  if (id) {
    return inject(DebitCardService)
      .find(id)
      .pipe(
        mergeMap((debitCard: HttpResponse<IDebitCard>) => {
          if (debitCard.body) {
            return of(debitCard.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default debitCardResolve;
