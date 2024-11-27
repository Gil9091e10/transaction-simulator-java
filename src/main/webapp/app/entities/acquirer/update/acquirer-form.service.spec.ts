import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../acquirer.test-samples';

import { AcquirerFormService } from './acquirer-form.service';

describe('Acquirer Form Service', () => {
  let service: AcquirerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AcquirerFormService);
  });

  describe('Service methods', () => {
    describe('createAcquirerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAcquirerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            socketUrl: expect.any(Object),
            email: expect.any(Object),
          }),
        );
      });

      it('passing IAcquirer should create a new form with FormGroup', () => {
        const formGroup = service.createAcquirerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            socketUrl: expect.any(Object),
            email: expect.any(Object),
          }),
        );
      });
    });

    describe('getAcquirer', () => {
      it('should return NewAcquirer for default Acquirer initial value', () => {
        const formGroup = service.createAcquirerFormGroup(sampleWithNewData);

        const acquirer = service.getAcquirer(formGroup) as any;

        expect(acquirer).toMatchObject(sampleWithNewData);
      });

      it('should return NewAcquirer for empty Acquirer initial value', () => {
        const formGroup = service.createAcquirerFormGroup();

        const acquirer = service.getAcquirer(formGroup) as any;

        expect(acquirer).toMatchObject({});
      });

      it('should return IAcquirer', () => {
        const formGroup = service.createAcquirerFormGroup(sampleWithRequiredData);

        const acquirer = service.getAcquirer(formGroup) as any;

        expect(acquirer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAcquirer should not enable id FormControl', () => {
        const formGroup = service.createAcquirerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAcquirer should disable id FormControl', () => {
        const formGroup = service.createAcquirerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
