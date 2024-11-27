import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAdvice } from '../advice.model';

@Component({
  standalone: true,
  selector: 'stp-advice-detail',
  templateUrl: './advice-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AdviceDetailComponent {
  advice = input<IAdvice | null>(null);

  previousState(): void {
    window.history.back();
  }
}
