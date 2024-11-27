import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMessageTypeIndicator, NewMessageTypeIndicator } from '../message-type-indicator.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMessageTypeIndicator for edit and NewMessageTypeIndicatorFormGroupInput for create.
 */
type MessageTypeIndicatorFormGroupInput = IMessageTypeIndicator | PartialWithRequiredKeyOf<NewMessageTypeIndicator>;

type MessageTypeIndicatorFormDefaults = Pick<NewMessageTypeIndicator, 'id'>;

type MessageTypeIndicatorFormGroupContent = {
  id: FormControl<IMessageTypeIndicator['id'] | NewMessageTypeIndicator['id']>;
  name: FormControl<IMessageTypeIndicator['name']>;
  code: FormControl<IMessageTypeIndicator['code']>;
};

export type MessageTypeIndicatorFormGroup = FormGroup<MessageTypeIndicatorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MessageTypeIndicatorFormService {
  createMessageTypeIndicatorFormGroup(
    messageTypeIndicator: MessageTypeIndicatorFormGroupInput = { id: null },
  ): MessageTypeIndicatorFormGroup {
    const messageTypeIndicatorRawValue = {
      ...this.getFormDefaults(),
      ...messageTypeIndicator,
    };
    return new FormGroup<MessageTypeIndicatorFormGroupContent>({
      id: new FormControl(
        { value: messageTypeIndicatorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(messageTypeIndicatorRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      code: new FormControl(messageTypeIndicatorRawValue.code, {
        validators: [Validators.required, Validators.maxLength(4)],
      }),
    });
  }

  getMessageTypeIndicator(form: MessageTypeIndicatorFormGroup): IMessageTypeIndicator | NewMessageTypeIndicator {
    return form.getRawValue() as IMessageTypeIndicator | NewMessageTypeIndicator;
  }

  resetForm(form: MessageTypeIndicatorFormGroup, messageTypeIndicator: MessageTypeIndicatorFormGroupInput): void {
    const messageTypeIndicatorRawValue = { ...this.getFormDefaults(), ...messageTypeIndicator };
    form.reset(
      {
        ...messageTypeIndicatorRawValue,
        id: { value: messageTypeIndicatorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MessageTypeIndicatorFormDefaults {
    return {
      id: null,
    };
  }
}
