import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IIssuer } from '../issuer.model';

@Component({
  standalone: true,
  selector: 'stp-issuer-detail',
  templateUrl: './issuer-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class IssuerDetailComponent {
  issuer = input<IIssuer | null>(null);

  previousState(): void {
    window.history.back();
  }
}
