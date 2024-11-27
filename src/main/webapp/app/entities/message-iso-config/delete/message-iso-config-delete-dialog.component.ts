import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMessageIsoConfig } from '../message-iso-config.model';
import { MessageIsoConfigService } from '../service/message-iso-config.service';

@Component({
  standalone: true,
  templateUrl: './message-iso-config-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MessageIsoConfigDeleteDialogComponent {
  messageIsoConfig?: IMessageIsoConfig;

  protected messageIsoConfigService = inject(MessageIsoConfigService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.messageIsoConfigService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
