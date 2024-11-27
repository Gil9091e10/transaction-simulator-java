import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdvice } from '../advice.model';
import { AdviceService } from '../service/advice.service';

const adviceResolve = (route: ActivatedRouteSnapshot): Observable<null | IAdvice> => {
  const id = route.params.id;
  if (id) {
    return inject(AdviceService)
      .find(id)
      .pipe(
        mergeMap((advice: HttpResponse<IAdvice>) => {
          if (advice.body) {
            return of(advice.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default adviceResolve;
