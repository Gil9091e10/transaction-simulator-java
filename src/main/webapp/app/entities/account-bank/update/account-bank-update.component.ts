import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { IAccountBank } from '../account-bank.model';
import { AccountBankService } from '../service/account-bank.service';
import { AccountBankFormGroup, AccountBankFormService } from './account-bank-form.service';

@Component({
  standalone: true,
  selector: 'stp-account-bank-update',
  templateUrl: './account-bank-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccountBankUpdateComponent implements OnInit {
  isSaving = false;
  accountBank: IAccountBank | null = null;

  currenciesSharedCollection: ICurrency[] = [];

  protected accountBankService = inject(AccountBankService);
  protected accountBankFormService = inject(AccountBankFormService);
  protected currencyService = inject(CurrencyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AccountBankFormGroup = this.accountBankFormService.createAccountBankFormGroup();

  compareCurrency = (o1: ICurrency | null, o2: ICurrency | null): boolean => this.currencyService.compareCurrency(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountBank }) => {
      this.accountBank = accountBank;
      if (accountBank) {
        this.updateForm(accountBank);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountBank = this.accountBankFormService.getAccountBank(this.editForm);
    if (accountBank.id !== null) {
      this.subscribeToSaveResponse(this.accountBankService.update(accountBank));
    } else {
      this.subscribeToSaveResponse(this.accountBankService.create(accountBank));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountBank>>): void {
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

  protected updateForm(accountBank: IAccountBank): void {
    this.accountBank = accountBank;
    this.accountBankFormService.resetForm(this.editForm, accountBank);

    this.currenciesSharedCollection = this.currencyService.addCurrencyToCollectionIfMissing<ICurrency>(
      this.currenciesSharedCollection,
      accountBank.currency,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.currencyService
      .query()
      .pipe(map((res: HttpResponse<ICurrency[]>) => res.body ?? []))
      .pipe(
        map((currencies: ICurrency[]) =>
          this.currencyService.addCurrencyToCollectionIfMissing<ICurrency>(currencies, this.accountBank?.currency),
        ),
      )
      .subscribe((currencies: ICurrency[]) => (this.currenciesSharedCollection = currencies));
  }
}
