import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICardType } from '../card-type.model';
import { CardTypeService } from '../service/card-type.service';
import { CardTypeFormGroup, CardTypeFormService } from './card-type-form.service';

@Component({
  standalone: true,
  selector: 'stp-card-type-update',
  templateUrl: './card-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CardTypeUpdateComponent implements OnInit {
  isSaving = false;
  cardType: ICardType | null = null;

  protected cardTypeService = inject(CardTypeService);
  protected cardTypeFormService = inject(CardTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CardTypeFormGroup = this.cardTypeFormService.createCardTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardType }) => {
      this.cardType = cardType;
      if (cardType) {
        this.updateForm(cardType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cardType = this.cardTypeFormService.getCardType(this.editForm);
    if (cardType.id !== null) {
      this.subscribeToSaveResponse(this.cardTypeService.update(cardType));
    } else {
      this.subscribeToSaveResponse(this.cardTypeService.create(cardType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICardType>>): void {
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

  protected updateForm(cardType: ICardType): void {
    this.cardType = cardType;
    this.cardTypeFormService.resetForm(this.editForm, cardType);
  }
}
