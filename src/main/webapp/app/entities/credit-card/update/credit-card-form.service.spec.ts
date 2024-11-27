import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../credit-card.test-samples';

import { CreditCardFormService } from './credit-card-form.service';

describe('CreditCard Form Service', () => {
  let service: CreditCardFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CreditCardFormService);
  });

  describe('Service methods', () => {
    describe('createCreditCardFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCreditCardFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            maxLimit: expect.any(Object),
          }),
        );
      });

      it('passing ICreditCard should create a new form with FormGroup', () => {
        const formGroup = service.createCreditCardFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            maxLimit: expect.any(Object),
          }),
        );
      });
    });

    describe('getCreditCard', () => {
      it('should return NewCreditCard for default CreditCard initial value', () => {
        const formGroup = service.createCreditCardFormGroup(sampleWithNewData);

        const creditCard = service.getCreditCard(formGroup) as any;

        expect(creditCard).toMatchObject(sampleWithNewData);
      });

      it('should return NewCreditCard for empty CreditCard initial value', () => {
        const formGroup = service.createCreditCardFormGroup();

        const creditCard = service.getCreditCard(formGroup) as any;

        expect(creditCard).toMatchObject({});
      });

      it('should return ICreditCard', () => {
        const formGroup = service.createCreditCardFormGroup(sampleWithRequiredData);

        const creditCard = service.getCreditCard(formGroup) as any;

        expect(creditCard).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICreditCard should not enable id FormControl', () => {
        const formGroup = service.createCreditCardFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCreditCard should disable id FormControl', () => {
        const formGroup = service.createCreditCardFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
