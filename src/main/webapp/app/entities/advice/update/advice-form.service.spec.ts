import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../advice.test-samples';

import { AdviceFormService } from './advice-form.service';

describe('Advice Form Service', () => {
  let service: AdviceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdviceFormService);
  });

  describe('Service methods', () => {
    describe('createAdviceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAdviceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            merchant: expect.any(Object),
            acquirer: expect.any(Object),
          }),
        );
      });

      it('passing IAdvice should create a new form with FormGroup', () => {
        const formGroup = service.createAdviceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            merchant: expect.any(Object),
            acquirer: expect.any(Object),
          }),
        );
      });
    });

    describe('getAdvice', () => {
      it('should return NewAdvice for default Advice initial value', () => {
        const formGroup = service.createAdviceFormGroup(sampleWithNewData);

        const advice = service.getAdvice(formGroup) as any;

        expect(advice).toMatchObject(sampleWithNewData);
      });

      it('should return NewAdvice for empty Advice initial value', () => {
        const formGroup = service.createAdviceFormGroup();

        const advice = service.getAdvice(formGroup) as any;

        expect(advice).toMatchObject({});
      });

      it('should return IAdvice', () => {
        const formGroup = service.createAdviceFormGroup(sampleWithRequiredData);

        const advice = service.getAdvice(formGroup) as any;

        expect(advice).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAdvice should not enable id FormControl', () => {
        const formGroup = service.createAdviceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAdvice should disable id FormControl', () => {
        const formGroup = service.createAdviceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
