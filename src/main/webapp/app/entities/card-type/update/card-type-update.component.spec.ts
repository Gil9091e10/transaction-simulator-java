import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CardTypeService } from '../service/card-type.service';
import { ICardType } from '../card-type.model';
import { CardTypeFormService } from './card-type-form.service';

import { CardTypeUpdateComponent } from './card-type-update.component';

describe('CardType Management Update Component', () => {
  let comp: CardTypeUpdateComponent;
  let fixture: ComponentFixture<CardTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cardTypeFormService: CardTypeFormService;
  let cardTypeService: CardTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CardTypeUpdateComponent],
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
      .overrideTemplate(CardTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CardTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cardTypeFormService = TestBed.inject(CardTypeFormService);
    cardTypeService = TestBed.inject(CardTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cardType: ICardType = { id: 456 };

      activatedRoute.data = of({ cardType });
      comp.ngOnInit();

      expect(comp.cardType).toEqual(cardType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICardType>>();
      const cardType = { id: 123 };
      jest.spyOn(cardTypeFormService, 'getCardType').mockReturnValue(cardType);
      jest.spyOn(cardTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardType }));
      saveSubject.complete();

      // THEN
      expect(cardTypeFormService.getCardType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cardTypeService.update).toHaveBeenCalledWith(expect.objectContaining(cardType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICardType>>();
      const cardType = { id: 123 };
      jest.spyOn(cardTypeFormService, 'getCardType').mockReturnValue({ id: null });
      jest.spyOn(cardTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cardType }));
      saveSubject.complete();

      // THEN
      expect(cardTypeFormService.getCardType).toHaveBeenCalled();
      expect(cardTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICardType>>();
      const cardType = { id: 123 };
      jest.spyOn(cardTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cardType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cardTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
