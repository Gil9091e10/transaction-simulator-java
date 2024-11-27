import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAcquirer } from 'app/entities/acquirer/acquirer.model';
import { AcquirerService } from 'app/entities/acquirer/service/acquirer.service';
import { IMessageTypeIndicator } from 'app/entities/message-type-indicator/message-type-indicator.model';
import { MessageTypeIndicatorService } from 'app/entities/message-type-indicator/service/message-type-indicator.service';
import { MessageIsoConfigService } from '../service/message-iso-config.service';
import { IMessageIsoConfig } from '../message-iso-config.model';
import { MessageIsoConfigFormGroup, MessageIsoConfigFormService } from './message-iso-config-form.service';

@Component({
  standalone: true,
  selector: 'stp-message-iso-config-update',
  templateUrl: './message-iso-config-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MessageIsoConfigUpdateComponent implements OnInit {
  isSaving = false;
  messageIsoConfig: IMessageIsoConfig | null = null;

  acquirersSharedCollection: IAcquirer[] = [];
  messageTypeIndicatorsSharedCollection: IMessageTypeIndicator[] = [];

  protected messageIsoConfigService = inject(MessageIsoConfigService);
  protected messageIsoConfigFormService = inject(MessageIsoConfigFormService);
  protected acquirerService = inject(AcquirerService);
  protected messageTypeIndicatorService = inject(MessageTypeIndicatorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MessageIsoConfigFormGroup = this.messageIsoConfigFormService.createMessageIsoConfigFormGroup();

  compareAcquirer = (o1: IAcquirer | null, o2: IAcquirer | null): boolean => this.acquirerService.compareAcquirer(o1, o2);

  compareMessageTypeIndicator = (o1: IMessageTypeIndicator | null, o2: IMessageTypeIndicator | null): boolean =>
    this.messageTypeIndicatorService.compareMessageTypeIndicator(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ messageIsoConfig }) => {
      this.messageIsoConfig = messageIsoConfig;
      if (messageIsoConfig) {
        this.updateForm(messageIsoConfig);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const messageIsoConfig = this.messageIsoConfigFormService.getMessageIsoConfig(this.editForm);
    if (messageIsoConfig.id !== null) {
      this.subscribeToSaveResponse(this.messageIsoConfigService.update(messageIsoConfig));
    } else {
      this.subscribeToSaveResponse(this.messageIsoConfigService.create(messageIsoConfig));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMessageIsoConfig>>): void {
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

  protected updateForm(messageIsoConfig: IMessageIsoConfig): void {
    this.messageIsoConfig = messageIsoConfig;
    this.messageIsoConfigFormService.resetForm(this.editForm, messageIsoConfig);

    this.acquirersSharedCollection = this.acquirerService.addAcquirerToCollectionIfMissing<IAcquirer>(
      this.acquirersSharedCollection,
      messageIsoConfig.acquirer,
    );
    this.messageTypeIndicatorsSharedCollection =
      this.messageTypeIndicatorService.addMessageTypeIndicatorToCollectionIfMissing<IMessageTypeIndicator>(
        this.messageTypeIndicatorsSharedCollection,
        messageIsoConfig.messageTypeIndicator,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.acquirerService
      .query()
      .pipe(map((res: HttpResponse<IAcquirer[]>) => res.body ?? []))
      .pipe(
        map((acquirers: IAcquirer[]) =>
          this.acquirerService.addAcquirerToCollectionIfMissing<IAcquirer>(acquirers, this.messageIsoConfig?.acquirer),
        ),
      )
      .subscribe((acquirers: IAcquirer[]) => (this.acquirersSharedCollection = acquirers));

    this.messageTypeIndicatorService
      .query()
      .pipe(map((res: HttpResponse<IMessageTypeIndicator[]>) => res.body ?? []))
      .pipe(
        map((messageTypeIndicators: IMessageTypeIndicator[]) =>
          this.messageTypeIndicatorService.addMessageTypeIndicatorToCollectionIfMissing<IMessageTypeIndicator>(
            messageTypeIndicators,
            this.messageIsoConfig?.messageTypeIndicator,
          ),
        ),
      )
      .subscribe((messageTypeIndicators: IMessageTypeIndicator[]) => (this.messageTypeIndicatorsSharedCollection = messageTypeIndicators));
  }
}
