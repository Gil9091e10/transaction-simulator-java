import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDebitCard } from '../debit-card.model';
import { DebitCardService } from '../service/debit-card.service';
import { DebitCardFormGroup, DebitCardFormService } from './debit-card-form.service';

@Component({
  standalone: true,
  selector: 'stp-debit-card-update',
  templateUrl: './debit-card-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DebitCardUpdateComponent implements OnInit {
  isSaving = false;
  debitCard: IDebitCard | null = null;

  protected debitCardService = inject(DebitCardService);
  protected debitCardFormService = inject(DebitCardFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DebitCardFormGroup = this.debitCardFormService.createDebitCardFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ debitCard }) => {
      this.debitCard = debitCard;
      if (debitCard) {
        this.updateForm(debitCard);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const debitCard = this.debitCardFormService.getDebitCard(this.editForm);
    this.subscribeToSaveResponse(this.debitCardService.create(debitCard));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDebitCard>>): void {
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

  protected updateForm(debitCard: IDebitCard): void {
    this.debitCard = debitCard;
    this.debitCardFormService.resetForm(this.editForm, debitCard);
  }
}
