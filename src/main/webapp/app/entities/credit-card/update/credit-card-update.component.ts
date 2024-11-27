import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICreditCard } from '../credit-card.model';
import { CreditCardService } from '../service/credit-card.service';
import { CreditCardFormGroup, CreditCardFormService } from './credit-card-form.service';

@Component({
  standalone: true,
  selector: 'stp-credit-card-update',
  templateUrl: './credit-card-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CreditCardUpdateComponent implements OnInit {
  isSaving = false;
  creditCard: ICreditCard | null = null;

  protected creditCardService = inject(CreditCardService);
  protected creditCardFormService = inject(CreditCardFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CreditCardFormGroup = this.creditCardFormService.createCreditCardFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ creditCard }) => {
      this.creditCard = creditCard;
      if (creditCard) {
        this.updateForm(creditCard);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const creditCard = this.creditCardFormService.getCreditCard(this.editForm);
    if (creditCard.id !== null) {
      this.subscribeToSaveResponse(this.creditCardService.update(creditCard));
    } else {
      this.subscribeToSaveResponse(this.creditCardService.create(creditCard));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICreditCard>>): void {
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

  protected updateForm(creditCard: ICreditCard): void {
    this.creditCard = creditCard;
    this.creditCardFormService.resetForm(this.editForm, creditCard);
  }
}
