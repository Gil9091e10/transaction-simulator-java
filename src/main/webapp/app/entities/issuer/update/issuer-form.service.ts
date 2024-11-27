import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IIssuer, NewIssuer } from '../issuer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIssuer for edit and NewIssuerFormGroupInput for create.
 */
type IssuerFormGroupInput = IIssuer | PartialWithRequiredKeyOf<NewIssuer>;

type IssuerFormDefaults = Pick<NewIssuer, 'id'>;

type IssuerFormGroupContent = {
  id: FormControl<IIssuer['id'] | NewIssuer['id']>;
  code: FormControl<IIssuer['code']>;
  name: FormControl<IIssuer['name']>;
};

export type IssuerFormGroup = FormGroup<IssuerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IssuerFormService {
  createIssuerFormGroup(issuer: IssuerFormGroupInput = { id: null }): IssuerFormGroup {
    const issuerRawValue = {
      ...this.getFormDefaults(),
      ...issuer,
    };
    return new FormGroup<IssuerFormGroupContent>({
      id: new FormControl(
        { value: issuerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(issuerRawValue.code, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
      name: new FormControl(issuerRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
    });
  }

  getIssuer(form: IssuerFormGroup): IIssuer | NewIssuer {
    return form.getRawValue() as IIssuer | NewIssuer;
  }

  resetForm(form: IssuerFormGroup, issuer: IssuerFormGroupInput): void {
    const issuerRawValue = { ...this.getFormDefaults(), ...issuer };
    form.reset(
      {
        ...issuerRawValue,
        id: { value: issuerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): IssuerFormDefaults {
    return {
      id: null,
    };
  }
}
