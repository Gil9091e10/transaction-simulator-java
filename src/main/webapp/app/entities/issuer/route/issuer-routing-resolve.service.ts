import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIssuer } from '../issuer.model';
import { IssuerService } from '../service/issuer.service';

const issuerResolve = (route: ActivatedRouteSnapshot): Observable<null | IIssuer> => {
  const id = route.params.id;
  if (id) {
    return inject(IssuerService)
      .find(id)
      .pipe(
        mergeMap((issuer: HttpResponse<IIssuer>) => {
          if (issuer.body) {
            return of(issuer.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default issuerResolve;
