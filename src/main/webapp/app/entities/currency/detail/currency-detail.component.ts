import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICurrency } from '../currency.model';

@Component({
  standalone: true,
  selector: 'stp-currency-detail',
  templateUrl: './currency-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CurrencyDetailComponent {
  currency = input<ICurrency | null>(null);

  previousState(): void {
    window.history.back();
  }
}
