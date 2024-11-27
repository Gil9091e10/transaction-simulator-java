import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../message-iso-config.test-samples';

import { MessageIsoConfigFormService } from './message-iso-config-form.service';

describe('MessageIsoConfig Form Service', () => {
  let service: MessageIsoConfigFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MessageIsoConfigFormService);
  });

  describe('Service methods', () => {
    describe('createMessageIsoConfigFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMessageIsoConfigFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            filename: expect.any(Object),
            acquirer: expect.any(Object),
            messageTypeIndicator: expect.any(Object),
          }),
        );
      });

      it('passing IMessageIsoConfig should create a new form with FormGroup', () => {
        const formGroup = service.createMessageIsoConfigFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            filename: expect.any(Object),
            acquirer: expect.any(Object),
            messageTypeIndicator: expect.any(Object),
          }),
        );
      });
    });

    describe('getMessageIsoConfig', () => {
      it('should return NewMessageIsoConfig for default MessageIsoConfig initial value', () => {
        const formGroup = service.createMessageIsoConfigFormGroup(sampleWithNewData);

        const messageIsoConfig = service.getMessageIsoConfig(formGroup) as any;

        expect(messageIsoConfig).toMatchObject(sampleWithNewData);
      });

      it('should return NewMessageIsoConfig for empty MessageIsoConfig initial value', () => {
        const formGroup = service.createMessageIsoConfigFormGroup();

        const messageIsoConfig = service.getMessageIsoConfig(formGroup) as any;

        expect(messageIsoConfig).toMatchObject({});
      });

      it('should return IMessageIsoConfig', () => {
        const formGroup = service.createMessageIsoConfigFormGroup(sampleWithRequiredData);

        const messageIsoConfig = service.getMessageIsoConfig(formGroup) as any;

        expect(messageIsoConfig).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMessageIsoConfig should not enable id FormControl', () => {
        const formGroup = service.createMessageIsoConfigFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMessageIsoConfig should disable id FormControl', () => {
        const formGroup = service.createMessageIsoConfigFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
