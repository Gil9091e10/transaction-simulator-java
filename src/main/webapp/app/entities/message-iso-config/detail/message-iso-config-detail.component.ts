import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMessageIsoConfig } from '../message-iso-config.model';

@Component({
  standalone: true,
  selector: 'stp-message-iso-config-detail',
  templateUrl: './message-iso-config-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MessageIsoConfigDetailComponent {
  messageIsoConfig = input<IMessageIsoConfig | null>(null);

  previousState(): void {
    window.history.back();
  }
}
