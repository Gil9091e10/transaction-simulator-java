import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAcquirer } from '../acquirer.model';

@Component({
  standalone: true,
  selector: 'stp-acquirer-detail',
  templateUrl: './acquirer-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AcquirerDetailComponent {
  acquirer = input<IAcquirer | null>(null);

  previousState(): void {
    window.history.back();
  }
}