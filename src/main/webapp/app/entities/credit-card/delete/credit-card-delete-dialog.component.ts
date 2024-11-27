import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICreditCard } from '../credit-card.model';
import { CreditCardService } from '../service/credit-card.service';

@Component({
  standalone: true,
  templateUrl: './credit-card-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CreditCardDeleteDialogComponent {
  creditCard?: ICreditCard;

  protected creditCardService = inject(CreditCardService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.creditCardService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
