import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAcquirer } from '../acquirer.model';
import { AcquirerService } from '../service/acquirer.service';

const acquirerResolve = (route: ActivatedRouteSnapshot): Observable<null | IAcquirer> => {
  const id = route.params.id;
  if (id) {
    return inject(AcquirerService)
      .find(id)
      .pipe(
        mergeMap((acquirer: HttpResponse<IAcquirer>) => {
          if (acquirer.body) {
            return of(acquirer.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default acquirerResolve;
