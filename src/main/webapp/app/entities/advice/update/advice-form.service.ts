import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAdvice, NewAdvice } from '../advice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdvice for edit and NewAdviceFormGroupInput for create.
 */
type AdviceFormGroupInput = IAdvice | PartialWithRequiredKeyOf<NewAdvice>;

type AdviceFormDefaults = Pick<NewAdvice, 'id'>;

type AdviceFormGroupContent = {
  id: FormControl<IAdvice['id'] | NewAdvice['id']>;
  code: FormControl<IAdvice['code']>;
  name: FormControl<IAdvice['name']>;
  merchant: FormControl<IAdvice['merchant']>;
  acquirer: FormControl<IAdvice['acquirer']>;
};

export type AdviceFormGroup = FormGroup<AdviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdviceFormService {
  createAdviceFormGroup(advice: AdviceFormGroupInput = { id: null }): AdviceFormGroup {
    const adviceRawValue = {
      ...this.getFormDefaults(),
      ...advice,
    };
    return new FormGroup<AdviceFormGroupContent>({
      id: new FormControl(
        { value: adviceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(adviceRawValue.code, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
      name: new FormControl(adviceRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      merchant: new FormControl(adviceRawValue.merchant),
      acquirer: new FormControl(adviceRawValue.acquirer),
    });
  }

  getAdvice(form: AdviceFormGroup): IAdvice | NewAdvice {
    return form.getRawValue() as IAdvice | NewAdvice;
  }

  resetForm(form: AdviceFormGroup, advice: AdviceFormGroupInput): void {
    const adviceRawValue = { ...this.getFormDefaults(), ...advice };
    form.reset(
      {
        ...adviceRawValue,
        id: { value: adviceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AdviceFormDefaults {
    return {
      id: null,
    };
  }
}
