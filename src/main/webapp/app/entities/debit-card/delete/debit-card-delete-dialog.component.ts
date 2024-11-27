import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDebitCard } from '../debit-card.model';
import { DebitCardService } from '../service/debit-card.service';

@Component({
  standalone: true,
  templateUrl: './debit-card-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DebitCardDeleteDialogComponent {
  debitCard?: IDebitCard;

  protected debitCardService = inject(DebitCardService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.debitCardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
