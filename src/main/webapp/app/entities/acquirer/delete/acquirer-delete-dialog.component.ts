import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAcquirer } from '../acquirer.model';
import { AcquirerService } from '../service/acquirer.service';

@Component({
  standalone: true,
  templateUrl: './acquirer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AcquirerDeleteDialogComponent {
  acquirer?: IAcquirer;

  protected acquirerService = inject(AcquirerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.acquirerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
