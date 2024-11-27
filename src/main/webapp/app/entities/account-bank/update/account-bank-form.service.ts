import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAccountBank, NewAccountBank } from '../account-bank.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccountBank for edit and NewAccountBankFormGroupInput for create.
 */
type AccountBankFormGroupInput = IAccountBank | PartialWithRequiredKeyOf<NewAccountBank>;

type AccountBankFormDefaults = Pick<NewAccountBank, 'id'>;

type AccountBankFormGroupContent = {
  id: FormControl<IAccountBank['id'] | NewAccountBank['id']>;
  number: FormControl<IAccountBank['number']>;
  numberIBAN: FormControl<IAccountBank['numberIBAN']>;
  balance: FormControl<IAccountBank['balance']>;
  currency: FormControl<IAccountBank['currency']>;
};

export type AccountBankFormGroup = FormGroup<AccountBankFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccountBankFormService {
  createAccountBankFormGroup(accountBank: AccountBankFormGroupInput = { id: null }): AccountBankFormGroup {
    const accountBankRawValue = {
      ...this.getFormDefaults(),
      ...accountBank,
    };
    return new FormGroup<AccountBankFormGroupContent>({
      id: new FormControl(
        { value: accountBankRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      number: new FormControl(accountBankRawValue.number, {
        validators: [Validators.required],
      }),
      numberIBAN: new FormControl(accountBankRawValue.numberIBAN, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      balance: new FormControl(accountBankRawValue.balance, {
        validators: [Validators.required],
      }),
      currency: new FormControl(accountBankRawValue.currency),
    });
  }

  getAccountBank(form: AccountBankFormGroup): IAccountBank | NewAccountBank {
    return form.getRawValue() as IAccountBank | NewAccountBank;
  }

  resetForm(form: AccountBankFormGroup, accountBank: AccountBankFormGroupInput): void {
    const accountBankRawValue = { ...this.getFormDefaults(), ...accountBank };
    form.reset(
      {
        ...accountBankRawValue,
        id: { value: accountBankRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccountBankFormDefaults {
    return {
      id: null,
    };
  }
}
