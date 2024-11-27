import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITransactionType } from '../transaction-type.model';

@Component({
  standalone: true,
  selector: 'stp-transaction-type-detail',
  templateUrl: './transaction-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TransactionTypeDetailComponent {
  transactionType = input<ITransactionType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
