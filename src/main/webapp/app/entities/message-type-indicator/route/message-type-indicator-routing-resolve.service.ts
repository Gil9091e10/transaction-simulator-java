import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMessageTypeIndicator } from '../message-type-indicator.model';
import { MessageTypeIndicatorService } from '../service/message-type-indicator.service';

const messageTypeIndicatorResolve = (route: ActivatedRouteSnapshot): Observable<null | IMessageTypeIndicator> => {
  const id = route.params.id;
  if (id) {
    return inject(MessageTypeIndicatorService)
      .find(id)
      .pipe(
        mergeMap((messageTypeIndicator: HttpResponse<IMessageTypeIndicator>) => {
          if (messageTypeIndicator.body) {
            return of(messageTypeIndicator.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default messageTypeIndicatorResolve;
