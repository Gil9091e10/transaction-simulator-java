import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAccountBank } from '../account-bank.model';

@Component({
  standalone: true,
  selector: 'stp-account-bank-detail',
  templateUrl: './account-bank-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AccountBankDetailComponent {
  accountBank = input<IAccountBank | null>(null);

  previousState(): void {
    window.history.back();
  }
}
