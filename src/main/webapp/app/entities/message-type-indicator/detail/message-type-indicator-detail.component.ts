import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMessageTypeIndicator } from '../message-type-indicator.model';

@Component({
  standalone: true,
  selector: 'stp-message-type-indicator-detail',
  templateUrl: './message-type-indicator-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MessageTypeIndicatorDetailComponent {
  messageTypeIndicator = input<IMessageTypeIndicator | null>(null);

  previousState(): void {
    window.history.back();
  }
}
