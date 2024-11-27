import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAdvice } from '../advice.model';
import { AdviceService } from '../service/advice.service';

@Component({
  standalone: true,
  templateUrl: './advice-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AdviceDeleteDialogComponent {
  advice?: IAdvice;

  protected adviceService = inject(AdviceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adviceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
