import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../issuer.test-samples';

import { IssuerFormService } from './issuer-form.service';

describe('Issuer Form Service', () => {
  let service: IssuerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IssuerFormService);
  });

  describe('Service methods', () => {
    describe('createIssuerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIssuerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IIssuer should create a new form with FormGroup', () => {
        const formGroup = service.createIssuerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getIssuer', () => {
      it('should return NewIssuer for default Issuer initial value', () => {
        const formGroup = service.createIssuerFormGroup(sampleWithNewData);

        const issuer = service.getIssuer(formGroup) as any;

        expect(issuer).toMatchObject(sampleWithNewData);
      });

      it('should return NewIssuer for empty Issuer initial value', () => {
        const formGroup = service.createIssuerFormGroup();

        const issuer = service.getIssuer(formGroup) as any;

        expect(issuer).toMatchObject({});
      });

      it('should return IIssuer', () => {
        const formGroup = service.createIssuerFormGroup(sampleWithRequiredData);

        const issuer = service.getIssuer(formGroup) as any;

        expect(issuer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIssuer should not enable id FormControl', () => {
        const formGroup = service.createIssuerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIssuer should disable id FormControl', () => {
        const formGroup = service.createIssuerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
