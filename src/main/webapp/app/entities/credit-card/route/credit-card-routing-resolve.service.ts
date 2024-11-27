import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICreditCard } from '../credit-card.model';
import { CreditCardService } from '../service/credit-card.service';

const creditCardResolve = (route: ActivatedRouteSnapshot): Observable<null | ICreditCard> => {
  const id = route.params.id;
  if (id) {
    return inject(CreditCardService)
      .find(id)
      .pipe(
        mergeMap((creditCard: HttpResponse<ICreditCard>) => {
          if (creditCard.body) {
            return of(creditCard.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default creditCardResolve;
