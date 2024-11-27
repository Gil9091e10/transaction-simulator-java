import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDebitCard, NewDebitCard } from '../debit-card.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDebitCard for edit and NewDebitCardFormGroupInput for create.
 */
type DebitCardFormGroupInput = IDebitCard | PartialWithRequiredKeyOf<NewDebitCard>;

type DebitCardFormDefaults = Pick<NewDebitCard, 'id'>;

type DebitCardFormGroupContent = {
  id: FormControl<IDebitCard['id'] | NewDebitCard['id']>;
};

export type DebitCardFormGroup = FormGroup<DebitCardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DebitCardFormService {
  createDebitCardFormGroup(debitCard: DebitCardFormGroupInput = { id: null }): DebitCardFormGroup {
    const debitCardRawValue = {
      ...this.getFormDefaults(),
      ...debitCard,
    };
    return new FormGroup<DebitCardFormGroupContent>({
      id: new FormControl(
        { value: debitCardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
    });
  }

  getDebitCard(form: DebitCardFormGroup): NewDebitCard {
    return form.getRawValue() as NewDebitCard;
  }

  resetForm(form: DebitCardFormGroup, debitCard: DebitCardFormGroupInput): void {
    const debitCardRawValue = { ...this.getFormDefaults(), ...debitCard };
    form.reset(
      {
        ...debitCardRawValue,
        id: { value: debitCardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DebitCardFormDefaults {
    return {
      id: null,
    };
  }
}
