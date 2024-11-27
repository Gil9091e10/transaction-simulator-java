import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICardType } from '../card-type.model';

@Component({
  standalone: true,
  selector: 'stp-card-type-detail',
  templateUrl: './card-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CardTypeDetailComponent {
  cardType = input<ICardType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
