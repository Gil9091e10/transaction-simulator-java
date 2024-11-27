import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMessageTypeIndicator } from '../message-type-indicator.model';
import { MessageTypeIndicatorService } from '../service/message-type-indicator.service';
import { MessageTypeIndicatorFormGroup, MessageTypeIndicatorFormService } from './message-type-indicator-form.service';

@Component({
  standalone: true,
  selector: 'stp-message-type-indicator-update',
  templateUrl: './message-type-indicator-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MessageTypeIndicatorUpdateComponent implements OnInit {
  isSaving = false;
  messageTypeIndicator: IMessageTypeIndicator | null = null;

  protected messageTypeIndicatorService = inject(MessageTypeIndicatorService);
  protected messageTypeIndicatorFormService = inject(MessageTypeIndicatorFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MessageTypeIndicatorFormGroup = this.messageTypeIndicatorFormService.createMessageTypeIndicatorFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ messageTypeIndicator }) => {
      this.messageTypeIndicator = messageTypeIndicator;
      if (messageTypeIndicator) {
        this.updateForm(messageTypeIndicator);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const messageTypeIndicator = this.messageTypeIndicatorFormService.getMessageTypeIndicator(this.editForm);
    if (messageTypeIndicator.id !== null) {
      this.subscribeToSaveResponse(this.messageTypeIndicatorService.update(messageTypeIndicator));
    } else {
      this.subscribeToSaveResponse(this.messageTypeIndicatorService.create(messageTypeIndicator));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMessageTypeIndicator>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(messageTypeIndicator: IMessageTypeIndicator): void {
    this.messageTypeIndicator = messageTypeIndicator;
    this.messageTypeIndicatorFormService.resetForm(this.editForm, messageTypeIndicator);
  }
}
