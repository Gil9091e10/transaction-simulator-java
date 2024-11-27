import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../debit-card.test-samples';

import { DebitCardFormService } from './debit-card-form.service';

describe('DebitCard Form Service', () => {
  let service: DebitCardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DebitCardFormService);
  });

  describe('Service methods', () => {
    describe('createDebitCardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDebitCardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          }),
        );
      });

      it('passing IDebitCard should create a new form with FormGroup', () => {
        const formGroup = service.createDebitCardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          }),
        );
      });
    });

    describe('getDebitCard', () => {
      it('should return NewDebitCard for default DebitCard initial value', () => {
        const formGroup = service.createDebitCardFormGroup(sampleWithNewData);

        const debitCard = service.getDebitCard(formGroup) as any;

        expect(debitCard).toMatchObject(sampleWithNewData);
      });

      it('should return NewDebitCard for empty DebitCard initial value', () => {
        const formGroup = service.createDebitCardFormGroup();

        const debitCard = service.getDebitCard(formGroup) as any;

        expect(debitCard).toMatchObject({});
      });

      it('should return IDebitCard', () => {
        const formGroup = service.createDebitCardFormGroup(sampleWithRequiredData);

        const debitCard = service.getDebitCard(formGroup) as any;

        expect(debitCard).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDebitCard should not enable id FormControl', () => {
        const formGroup = service.createDebitCardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDebitCard should disable id FormControl', () => {
        const formGroup = service.createDebitCardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
