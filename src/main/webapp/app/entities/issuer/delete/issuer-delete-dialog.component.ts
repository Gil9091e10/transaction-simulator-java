import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIssuer } from '../issuer.model';
import { IssuerService } from '../service/issuer.service';

@Component({
  standalone: true,
  templateUrl: './issuer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class IssuerDeleteDialogComponent {
  issuer?: IIssuer;

  protected issuerService = inject(IssuerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.issuerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
