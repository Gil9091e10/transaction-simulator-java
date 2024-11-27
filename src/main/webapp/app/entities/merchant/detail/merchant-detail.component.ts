import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMerchant } from '../merchant.model';

@Component({
  standalone: true,
  selector: 'stp-merchant-detail',
  templateUrl: './merchant-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MerchantDetailComponent {
  merchant = input<IMerchant | null>(null);

  previousState(): void {
    window.history.back();
  }
}
