import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICardType } from '../card-type.model';
import { CardTypeService } from '../service/card-type.service';

const cardTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ICardType> => {
  const id = route.params.id;
  if (id) {
    return inject(CardTypeService)
      .find(id)
      .pipe(
        mergeMap((cardType: HttpResponse<ICardType>) => {
          if (cardType.body) {
            return of(cardType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default cardTypeResolve;
