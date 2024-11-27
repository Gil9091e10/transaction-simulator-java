import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDebitCard } from '../debit-card.model';

@Component({
  standalone: true,
  selector: 'stp-debit-card-detail',
  templateUrl: './debit-card-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DebitCardDetailComponent {
  debitCard = input<IDebitCard | null>(null);

  previousState(): void {
    window.history.back();
  }
}
