import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITransactionType } from '../transaction-type.model';
import { TransactionTypeService } from '../service/transaction-type.service';
import { TransactionTypeFormGroup, TransactionTypeFormService } from './transaction-type-form.service';

@Component({
  standalone: true,
  selector: 'stp-transaction-type-update',
  templateUrl: './transaction-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransactionTypeUpdateComponent implements OnInit {
  isSaving = false;
  transactionType: ITransactionType | null = null;

  protected transactionTypeService = inject(TransactionTypeService);
  protected transactionTypeFormService = inject(TransactionTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionTypeFormGroup = this.transactionTypeFormService.createTransactionTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionType }) => {
      this.transactionType = transactionType;
      if (transactionType) {
        this.updateForm(transactionType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transactionType = this.transactionTypeFormService.getTransactionType(this.editForm);
    if (transactionType.id !== null) {
      this.subscribeToSaveResponse(this.transactionTypeService.update(transactionType));
    } else {
      this.subscribeToSaveResponse(this.transactionTypeService.create(transactionType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionType>>): void {
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

  protected updateForm(transactionType: ITransactionType): void {
    this.transactionType = transactionType;
    this.transactionTypeFormService.resetForm(this.editForm, transactionType);
  }
}
