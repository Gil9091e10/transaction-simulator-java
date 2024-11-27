import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAccountBank } from 'app/entities/account-bank/account-bank.model';
import { AccountBankService } from 'app/entities/account-bank/service/account-bank.service';
import { ICardType } from 'app/entities/card-type/card-type.model';
import { CardTypeService } from 'app/entities/card-type/service/card-type.service';
import { IIssuer } from 'app/entities/issuer/issuer.model';
import { IssuerService } from 'app/entities/issuer/service/issuer.service';
import { CardService } from '../service/card.service';
import { ICard } from '../card.model';
import { CardFormGroup, CardFormService } from './card-form.service';

@Component({
  standalone: true,
  selector: 'stp-card-update',
  templateUrl: './card-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CardUpdateComponent implements OnInit {
  isSaving = false;
  card: ICard | null = null;

  accountBanksCollection: IAccountBank[] = [];
  cardTypesSharedCollection: ICardType[] = [];
  issuersSharedCollection: IIssuer[] = [];

  protected cardService = inject(CardService);
  protected cardFormService = inject(CardFormService);
  protected accountBankService = inject(AccountBankService);
  protected cardTypeService = inject(CardTypeService);
  protected issuerService = inject(IssuerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CardFormGroup = this.cardFormService.createCardFormGroup();

  compareAccountBank = (o1: IAccountBank | null, o2: IAccountBank | null): boolean => this.accountBankService.compareAccountBank(o1, o2);

  compareCardType = (o1: ICardType | null, o2: ICardType | null): boolean => this.cardTypeService.compareCardType(o1, o2);

  compareIssuer = (o1: IIssuer | null, o2: IIssuer | null): boolean => this.issuerService.compareIssuer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ card }) => {
      this.card = card;
      if (card) {
        this.updateForm(card);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const card = this.cardFormService.getCard(this.editForm);
    if (card.id !== null) {
      this.subscribeToSaveResponse(this.cardService.update(card));
    } else {
      this.subscribeToSaveResponse(this.cardService.create(card));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICard>>): void {
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

  protected updateForm(card: ICard): void {
    this.card = card;
    this.cardFormService.resetForm(this.editForm, card);

    this.accountBanksCollection = this.accountBankService.addAccountBankToCollectionIfMissing<IAccountBank>(
      this.accountBanksCollection,
      card.accountBank,
    );
    this.cardTypesSharedCollection = this.cardTypeService.addCardTypeToCollectionIfMissing<ICardType>(
      this.cardTypesSharedCollection,
      card.cardType,
    );
    this.issuersSharedCollection = this.issuerService.addIssuerToCollectionIfMissing<IIssuer>(this.issuersSharedCollection, card.issuer);
  }

  protected loadRelationshipsOptions(): void {
    this.accountBankService
      .query({ 'cardId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAccountBank[]>) => res.body ?? []))
      .pipe(
        map((accountBanks: IAccountBank[]) =>
          this.accountBankService.addAccountBankToCollectionIfMissing<IAccountBank>(accountBanks, this.card?.accountBank),
        ),
      )
      .subscribe((accountBanks: IAccountBank[]) => (this.accountBanksCollection = accountBanks));

    this.cardTypeService
      .query()
      .pipe(map((res: HttpResponse<ICardType[]>) => res.body ?? []))
      .pipe(
        map((cardTypes: ICardType[]) => this.cardTypeService.addCardTypeToCollectionIfMissing<ICardType>(cardTypes, this.card?.cardType)),
      )
      .subscribe((cardTypes: ICardType[]) => (this.cardTypesSharedCollection = cardTypes));

    this.issuerService
      .query()
      .pipe(map((res: HttpResponse<IIssuer[]>) => res.body ?? []))
      .pipe(map((issuers: IIssuer[]) => this.issuerService.addIssuerToCollectionIfMissing<IIssuer>(issuers, this.card?.issuer)))
      .subscribe((issuers: IIssuer[]) => (this.issuersSharedCollection = issuers));
  }
}
