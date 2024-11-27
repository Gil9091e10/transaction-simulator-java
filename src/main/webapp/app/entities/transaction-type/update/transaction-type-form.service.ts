import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITransactionType, NewTransactionType } from '../transaction-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransactionType for edit and NewTransactionTypeFormGroupInput for create.
 */
type TransactionTypeFormGroupInput = ITransactionType | PartialWithRequiredKeyOf<NewTransactionType>;

type TransactionTypeFormDefaults = Pick<NewTransactionType, 'id'>;

type TransactionTypeFormGroupContent = {
  id: FormControl<ITransactionType['id'] | NewTransactionType['id']>;
  code: FormControl<ITransactionType['code']>;
  name: FormControl<ITransactionType['name']>;
};

export type TransactionTypeFormGroup = FormGroup<TransactionTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransactionTypeFormService {
  createTransactionTypeFormGroup(transactionType: TransactionTypeFormGroupInput = { id: null }): TransactionTypeFormGroup {
    const transactionTypeRawValue = {
      ...this.getFormDefaults(),
      ...transactionType,
    };
    return new FormGroup<TransactionTypeFormGroupContent>({
      id: new FormControl(
        { value: transactionTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(transactionTypeRawValue.code, {
        validators: [Validators.required, Validators.maxLength(5)],
      }),
      name: new FormControl(transactionTypeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
    });
  }

  getTransactionType(form: TransactionTypeFormGroup): ITransactionType | NewTransactionType {
    return form.getRawValue() as ITransactionType | NewTransactionType;
  }

  resetForm(form: TransactionTypeFormGroup, transactionType: TransactionTypeFormGroupInput): void {
    const transactionTypeRawValue = { ...this.getFormDefaults(), ...transactionType };
    form.reset(
      {
        ...transactionTypeRawValue,
        id: { value: transactionTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransactionTypeFormDefaults {
    return {
      id: null,
    };
  }
}
