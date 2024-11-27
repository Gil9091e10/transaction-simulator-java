import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICurrency } from 'app/entities/currency/currency.model';
import { CurrencyService } from 'app/entities/currency/service/currency.service';
import { AccountBankService } from '../service/account-bank.service';
import { IAccountBank } from '../account-bank.model';
import { AccountBankFormService } from './account-bank-form.service';

import { AccountBankUpdateComponent } from './account-bank-update.component';

describe('AccountBank Management Update Component', () => {
  let comp: AccountBankUpdateComponent;
  let fixture: ComponentFixture<AccountBankUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountBankFormService: AccountBankFormService;
  let accountBankService: AccountBankService;
  let currencyService: CurrencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AccountBankUpdateComponent],
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
      .overrideTemplate(AccountBankUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountBankUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountBankFormService = TestBed.inject(AccountBankFormService);
    accountBankService = TestBed.inject(AccountBankService);
    currencyService = TestBed.inject(CurrencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Currency query and add missing value', () => {
      const accountBank: IAccountBank = { id: 456 };
      const currency: ICurrency = { id: 1049 };
      accountBank.currency = currency;

      const currencyCollection: ICurrency[] = [{ id: 25924 }];
      jest.spyOn(currencyService, 'query').mockReturnValue(of(new HttpResponse({ body: currencyCollection })));
      const additionalCurrencies = [currency];
      const expectedCollection: ICurrency[] = [...additionalCurrencies, ...currencyCollection];
      jest.spyOn(currencyService, 'addCurrencyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountBank });
      comp.ngOnInit();

      expect(currencyService.query).toHaveBeenCalled();
      expect(currencyService.addCurrencyToCollectionIfMissing).toHaveBeenCalledWith(
        currencyCollection,
        ...additionalCurrencies.map(expect.objectContaining),
      );
      expect(comp.currenciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accountBank: IAccountBank = { id: 456 };
      const currency: ICurrency = { id: 27700 };
      accountBank.currency = currency;

      activatedRoute.data = of({ accountBank });
      comp.ngOnInit();

      expect(comp.currenciesSharedCollection).toContain(currency);
      expect(comp.accountBank).toEqual(accountBank);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountBank>>();
      const accountBank = { id: 123 };
      jest.spyOn(accountBankFormService, 'getAccountBank').mockReturnValue(accountBank);
      jest.spyOn(accountBankService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountBank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountBank }));
      saveSubject.complete();

      // THEN
      expect(accountBankFormService.getAccountBank).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountBankService.update).toHaveBeenCalledWith(expect.objectContaining(accountBank));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountBank>>();
      const accountBank = { id: 123 };
      jest.spyOn(accountBankFormService, 'getAccountBank').mockReturnValue({ id: null });
      jest.spyOn(accountBankService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountBank: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountBank }));
      saveSubject.complete();

      // THEN
      expect(accountBankFormService.getAccountBank).toHaveBeenCalled();
      expect(accountBankService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountBank>>();
      const accountBank = { id: 123 };
      jest.spyOn(accountBankService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountBank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountBankService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCurrency', () => {
      it('Should forward to currencyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(currencyService, 'compareCurrency');
        comp.compareCurrency(entity, entity2);
        expect(currencyService.compareCurrency).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
