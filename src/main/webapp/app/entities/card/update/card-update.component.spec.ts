import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAccountBank } from 'app/entities/account-bank/account-bank.model';
import { AccountBankService } from 'app/entities/account-bank/service/account-bank.service';
import { ICardType } from 'app/entities/card-type/card-type.model';
import { CardTypeService } from 'app/entities/card-type/service/card-type.service';
import { IIssuer } from 'app/entities/issuer/issuer.model';
import { IssuerService } from 'app/entities/issuer/service/issuer.service';
import { ICard } from '../card.model';
import { CardService } from '../service/card.service';
import { CardFormService } from './card-form.service';

import { CardUpdateComponent } from './card-update.component';

describe('Card Management Update Component', () => {
  let comp: CardUpdateComponent;
  let fixture: ComponentFixture<CardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardFormService: CardFormService;
  let cardService: CardService;
  let accountBankService: AccountBankService;
  let cardTypeService: CardTypeService;
  let issuerService: IssuerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CardUpdateComponent],
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
      .overrideTemplate(CardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardFormService = TestBed.inject(CardFormService);
    cardService = TestBed.inject(CardService);
    accountBankService = TestBed.inject(AccountBankService);
    cardTypeService = TestBed.inject(CardTypeService);
    issuerService = TestBed.inject(IssuerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call accountBank query and add missing value', () => {
      const card: ICard = { id: 456 };
      const accountBank: IAccountBank = { id: 30526 };
      card.accountBank = accountBank;

      const accountBankCollection: IAccountBank[] = [{ id: 3993 }];
      jest.spyOn(accountBankService, 'query').mockReturnValue(of(new HttpResponse({ body: accountBankCollection })));
      const expectedCollection: IAccountBank[] = [accountBank, ...accountBankCollection];
      jest.spyOn(accountBankService, 'addAccountBankToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ card });
      comp.ngOnInit();

      expect(accountBankService.query).toHaveBeenCalled();
      expect(accountBankService.addAccountBankToCollectionIfMissing).toHaveBeenCalledWith(accountBankCollection, accountBank);
      expect(comp.accountBanksCollection).toEqual(expectedCollection);
    });

    it('Should call CardType query and add missing value', () => {
      const card: ICard = { id: 456 };
      const cardType: ICardType = { id: 3515 };
      card.cardType = cardType;

      const cardTypeCollection: ICardType[] = [{ id: 6593 }];
      jest.spyOn(cardTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: cardTypeCollection })));
      const additionalCardTypes = [cardType];
      const expectedCollection: ICardType[] = [...additionalCardTypes, ...cardTypeCollection];
      jest.spyOn(cardTypeService, 'addCardTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ card });
      comp.ngOnInit();

      expect(cardTypeService.query).toHaveBeenCalled();
      expect(cardTypeService.addCardTypeToCollectionIfMissing).toHaveBeenCalledWith(
        cardTypeCollection,
        ...additionalCardTypes.map(expect.objectContaining),
      );
      expect(comp.cardTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Issuer query and add missing value', () => {
      const card: ICard = { id: 456 };
      const issuer: IIssuer = { id: 8950 };
      card.issuer = issuer;

      const issuerCollection: IIssuer[] = [{ id: 14641 }];
      jest.spyOn(issuerService, 'query').mockReturnValue(of(new HttpResponse({ body: issuerCollection })));
      const additionalIssuers = [issuer];
      const expectedCollection: IIssuer[] = [...additionalIssuers, ...issuerCollection];
      jest.spyOn(issuerService, 'addIssuerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ card });
      comp.ngOnInit();

      expect(issuerService.query).toHaveBeenCalled();
      expect(issuerService.addIssuerToCollectionIfMissing).toHaveBeenCalledWith(
        issuerCollection,
        ...additionalIssuers.map(expect.objectContaining),
      );
      expect(comp.issuersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const card: ICard = { id: 456 };
      const accountBank: IAccountBank = { id: 24176 };
      card.accountBank = accountBank;
      const cardType: ICardType = { id: 10075 };
      card.cardType = cardType;
      const issuer: IIssuer = { id: 15036 };
      card.issuer = issuer;

      activatedRoute.data = of({ card });
      comp.ngOnInit();

      expect(comp.accountBanksCollection).toContain(accountBank);
      expect(comp.cardTypesSharedCollection).toContain(cardType);
      expect(comp.issuersSharedCollection).toContain(issuer);
      expect(comp.card).toEqual(card);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICard>>();
      const card = { id: 123 };
      jest.spyOn(cardFormService, 'getCard').mockReturnValue(card);
      jest.spyOn(cardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ card });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: card }));
      saveSubject.complete();

      // THEN
      expect(cardFormService.getCard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardService.update).toHaveBeenCalledWith(expect.objectContaining(card));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICard>>();
      const card = { id: 123 };
      jest.spyOn(cardFormService, 'getCard').mockReturnValue({ id: null });
      jest.spyOn(cardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ card: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: card }));
      saveSubject.complete();

      // THEN
      expect(cardFormService.getCard).toHaveBeenCalled();
      expect(cardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICard>>();
      const card = { id: 123 };
      jest.spyOn(cardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ card });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAccountBank', () => {
      it('Should forward to accountBankService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(accountBankService, 'compareAccountBank');
        comp.compareAccountBank(entity, entity2);
        expect(accountBankService.compareAccountBank).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCardType', () => {
      it('Should forward to cardTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cardTypeService, 'compareCardType');
        comp.compareCardType(entity, entity2);
        expect(cardTypeService.compareCardType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareIssuer', () => {
      it('Should forward to issuerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(issuerService, 'compareIssuer');
        comp.compareIssuer(entity, entity2);
        expect(issuerService.compareIssuer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
