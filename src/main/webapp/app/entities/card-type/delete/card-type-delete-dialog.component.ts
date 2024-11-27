import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICardType } from '../card-type.model';
import { CardTypeService } from '../service/card-type.service';

@Component({
  standalone: true,
  templateUrl: './card-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CardTypeDeleteDialogComponent {
  cardType?: ICardType;

  protected cardTypeService = inject(CardTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
