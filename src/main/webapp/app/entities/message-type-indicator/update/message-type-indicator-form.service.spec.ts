import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../message-type-indicator.test-samples';

import { MessageTypeIndicatorFormService } from './message-type-indicator-form.service';

describe('MessageTypeIndicator Form Service', () => {
  let service: MessageTypeIndicatorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MessageTypeIndicatorFormService);
  });

  describe('Service methods', () => {
    describe('createMessageTypeIndicatorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMessageTypeIndicatorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
          }),
        );
      });

      it('passing IMessageTypeIndicator should create a new form with FormGroup', () => {
        const formGroup = service.createMessageTypeIndicatorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            code: expect.any(Object),
          }),
        );
      });
    });

    describe('getMessageTypeIndicator', () => {
      it('should return NewMessageTypeIndicator for default MessageTypeIndicator initial value', () => {
        const formGroup = service.createMessageTypeIndicatorFormGroup(sampleWithNewData);

        const messageTypeIndicator = service.getMessageTypeIndicator(formGroup) as any;

        expect(messageTypeIndicator).toMatchObject(sampleWithNewData);
      });

      it('should return NewMessageTypeIndicator for empty MessageTypeIndicator initial value', () => {
        const formGroup = service.createMessageTypeIndicatorFormGroup();

        const messageTypeIndicator = service.getMessageTypeIndicator(formGroup) as any;

        expect(messageTypeIndicator).toMatchObject({});
      });

      it('should return IMessageTypeIndicator', () => {
        const formGroup = service.createMessageTypeIndicatorFormGroup(sampleWithRequiredData);

        const messageTypeIndicator = service.getMessageTypeIndicator(formGroup) as any;

        expect(messageTypeIndicator).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMessageTypeIndicator should not enable id FormControl', () => {
        const formGroup = service.createMessageTypeIndicatorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMessageTypeIndicator should disable id FormControl', () => {
        const formGroup = service.createMessageTypeIndicatorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
