import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMessageIsoConfig } from '../message-iso-config.model';
import { MessageIsoConfigService } from '../service/message-iso-config.service';

const messageIsoConfigResolve = (route: ActivatedRouteSnapshot): Observable<null | IMessageIsoConfig> => {
  const id = route.params.id;
  if (id) {
    return inject(MessageIsoConfigService)
      .find(id)
      .pipe(
        mergeMap((messageIsoConfig: HttpResponse<IMessageIsoConfig>) => {
          if (messageIsoConfig.body) {
            return of(messageIsoConfig.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default messageIsoConfigResolve;
