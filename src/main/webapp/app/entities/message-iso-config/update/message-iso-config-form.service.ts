import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMessageIsoConfig, NewMessageIsoConfig } from '../message-iso-config.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMessageIsoConfig for edit and NewMessageIsoConfigFormGroupInput for create.
 */
type MessageIsoConfigFormGroupInput = IMessageIsoConfig | PartialWithRequiredKeyOf<NewMessageIsoConfig>;

type MessageIsoConfigFormDefaults = Pick<NewMessageIsoConfig, 'id'>;

type MessageIsoConfigFormGroupContent = {
  id: FormControl<IMessageIsoConfig['id'] | NewMessageIsoConfig['id']>;
  name: FormControl<IMessageIsoConfig['name']>;
  filename: FormControl<IMessageIsoConfig['filename']>;
  acquirer: FormControl<IMessageIsoConfig['acquirer']>;
  messageTypeIndicator: FormControl<IMessageIsoConfig['messageTypeIndicator']>;
};

export type MessageIsoConfigFormGroup = FormGroup<MessageIsoConfigFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MessageIsoConfigFormService {
  createMessageIsoConfigFormGroup(messageIsoConfig: MessageIsoConfigFormGroupInput = { id: null }): MessageIsoConfigFormGroup {
    const messageIsoConfigRawValue = {
      ...this.getFormDefaults(),
      ...messageIsoConfig,
    };
    return new FormGroup<MessageIsoConfigFormGroupContent>({
      id: new FormControl(
        { value: messageIsoConfigRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(messageIsoConfigRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      filename: new FormControl(messageIsoConfigRawValue.filename, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      acquirer: new FormControl(messageIsoConfigRawValue.acquirer),
      messageTypeIndicator: new FormControl(messageIsoConfigRawValue.messageTypeIndicator),
    });
  }

  getMessageIsoConfig(form: MessageIsoConfigFormGroup): IMessageIsoConfig | NewMessageIsoConfig {
    return form.getRawValue() as IMessageIsoConfig | NewMessageIsoConfig;
  }

  resetForm(form: MessageIsoConfigFormGroup, messageIsoConfig: MessageIsoConfigFormGroupInput): void {
    const messageIsoConfigRawValue = { ...this.getFormDefaults(), ...messageIsoConfig };
    form.reset(
      {
        ...messageIsoConfigRawValue,
        id: { value: messageIsoConfigRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MessageIsoConfigFormDefaults {
    return {
      id: null,
    };
  }
}
