import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICreditCard } from '../credit-card.model';

@Component({
  standalone: true,
  selector: 'stp-credit-card-detail',
  templateUrl: './credit-card-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CreditCardDetailComponent {
  creditCard = input<ICreditCard | null>(null);

  previousState(): void {
    window.history.back();
  }
}
