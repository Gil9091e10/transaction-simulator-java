import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAcquirer, NewAcquirer } from '../acquirer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAcquirer for edit and NewAcquirerFormGroupInput for create.
 */
type AcquirerFormGroupInput = IAcquirer | PartialWithRequiredKeyOf<NewAcquirer>;

type AcquirerFormDefaults = Pick<NewAcquirer, 'id'>;

type AcquirerFormGroupContent = {
  id: FormControl<IAcquirer['id'] | NewAcquirer['id']>;
  name: FormControl<IAcquirer['name']>;
  socketUrl: FormControl<IAcquirer['socketUrl']>;
  email: FormControl<IAcquirer['email']>;
};

export type AcquirerFormGroup = FormGroup<AcquirerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AcquirerFormService {
  createAcquirerFormGroup(acquirer: AcquirerFormGroupInput = { id: null }): AcquirerFormGroup {
    const acquirerRawValue = {
      ...this.getFormDefaults(),
      ...acquirer,
    };
    return new FormGroup<AcquirerFormGroupContent>({
      id: new FormControl(
        { value: acquirerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(acquirerRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      socketUrl: new FormControl(acquirerRawValue.socketUrl, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      email: new FormControl(acquirerRawValue.email, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
    });
  }

  getAcquirer(form: AcquirerFormGroup): IAcquirer | NewAcquirer {
    return form.getRawValue() as IAcquirer | NewAcquirer;
  }

  resetForm(form: AcquirerFormGroup, acquirer: AcquirerFormGroupInput): void {
    const acquirerRawValue = { ...this.getFormDefaults(), ...acquirer };
    form.reset(
      {
        ...acquirerRawValue,
        id: { value: acquirerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AcquirerFormDefaults {
    return {
      id: null,
    };
  }
}
