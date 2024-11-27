import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { IAcquirer } from 'app/entities/acquirer/acquirer.model';
import { AcquirerService } from 'app/entities/acquirer/service/acquirer.service';
import { IAdvice } from '../advice.model';
import { AdviceService } from '../service/advice.service';
import { AdviceFormService } from './advice-form.service';

import { AdviceUpdateComponent } from './advice-update.component';

describe('Advice Management Update Component', () => {
  let comp: AdviceUpdateComponent;
  let fixture: ComponentFixture<AdviceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let adviceFormService: AdviceFormService;
  let adviceService: AdviceService;
  let merchantService: MerchantService;
  let acquirerService: AcquirerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AdviceUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AdviceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdviceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    adviceFormService = TestBed.inject(AdviceFormService);
    adviceService = TestBed.inject(AdviceService);
    merchantService = TestBed.inject(MerchantService);
    acquirerService = TestBed.inject(AcquirerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Merchant query and add missing value', () => {
      const advice: IAdvice = { id: 456 };
      const merchant: IMerchant = { id: 16711 };
      advice.merchant = merchant;

      const merchantCollection: IMerchant[] = [{ id: 15771 }];
      jest.spyOn(merchantService, 'query').mockReturnValue(of(new HttpResponse({ body: merchantCollection })));
      const additionalMerchants = [merchant];
      const expectedCollection: IMerchant[] = [...additionalMerchants, ...merchantCollection];
      jest.spyOn(merchantService, 'addMerchantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ advice });
      comp.ngOnInit();

      expect(merchantService.query).toHaveBeenCalled();
      expect(merchantService.addMerchantToCollectionIfMissing).toHaveBeenCalledWith(
        merchantCollection,
        ...additionalMerchants.map(expect.objectContaining),
      );
      expect(comp.merchantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Acquirer query and add missing value', () => {
      const advice: IAdvice = { id: 456 };
      const acquirer: IAcquirer = { id: 2195 };
      advice.acquirer = acquirer;

      const acquirerCollection: IAcquirer[] = [{ id: 16970 }];
      jest.spyOn(acquirerService, 'query').mockReturnValue(of(new HttpResponse({ body: acquirerCollection })));
      const additionalAcquirers = [acquirer];
      const expectedCollection: IAcquirer[] = [...additionalAcquirers, ...acquirerCollection];
      jest.spyOn(acquirerService, 'addAcquirerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ advice });
      comp.ngOnInit();

      expect(acquirerService.query).toHaveBeenCalled();
      expect(acquirerService.addAcquirerToCollectionIfMissing).toHaveBeenCalledWith(
        acquirerCollection,
        ...additionalAcquirers.map(expect.objectContaining),
      );
      expect(comp.acquirersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const advice: IAdvice = { id: 456 };
      const merchant: IMerchant = { id: 6630 };
      advice.merchant = merchant;
      const acquirer: IAcquirer = { id: 23148 };
      advice.acquirer = acquirer;

      activatedRoute.data = of({ advice });
      comp.ngOnInit();

      expect(comp.merchantsSharedCollection).toContain(merchant);
      expect(comp.acquirersSharedCollection).toContain(acquirer);
      expect(comp.advice).toEqual(advice);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdvice>>();
      const advice = { id: 123 };
      jest.spyOn(adviceFormService, 'getAdvice').mockReturnValue(advice);
      jest.spyOn(adviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ advice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: advice }));
      saveSubject.complete();

      // THEN
      expect(adviceFormService.getAdvice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(adviceService.update).toHaveBeenCalledWith(expect.objectContaining(advice));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdvice>>();
      const advice = { id: 123 };
      jest.spyOn(adviceFormService, 'getAdvice').mockReturnValue({ id: null });
      jest.spyOn(adviceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ advice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: advice }));
      saveSubject.complete();

      // THEN
      expect(adviceFormService.getAdvice).toHaveBeenCalled();
      expect(adviceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdvice>>();
      const advice = { id: 123 };
      jest.spyOn(adviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ advice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(adviceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMerchant', () => {
      it('Should forward to merchantService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(merchantService, 'compareMerchant');
        comp.compareMerchant(entity, entity2);
        expect(merchantService.compareMerchant).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAcquirer', () => {
      it('Should forward to acquirerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(acquirerService, 'compareAcquirer');
        comp.compareAcquirer(entity, entity2);
        expect(acquirerService.compareAcquirer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
