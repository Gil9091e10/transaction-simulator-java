import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CreditCardService } from '../service/credit-card.service';
import { ICreditCard } from '../credit-card.model';
import { CreditCardFormService } from './credit-card-form.service';

import { CreditCardUpdateComponent } from './credit-card-update.component';

describe('CreditCard Management Update Component', () => {
  let comp: CreditCardUpdateComponent;
  let fixture: ComponentFixture<CreditCardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let creditCardFormService: CreditCardFormService;
  let creditCardService: CreditCardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CreditCardUpdateComponent],
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
      .overrideTemplate(CreditCardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CreditCardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    creditCardFormService = TestBed.inject(CreditCardFormService);
    creditCardService = TestBed.inject(CreditCardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const creditCard: ICreditCard = { id: 456 };

      activatedRoute.data = of({ creditCard });
      comp.ngOnInit();

      expect(comp.creditCard).toEqual(creditCard);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICreditCard>>();
      const creditCard = { id: 123 };
      jest.spyOn(creditCardFormService, 'getCreditCard').mockReturnValue(creditCard);
      jest.spyOn(creditCardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: creditCard }));
      saveSubject.complete();

      // THEN
      expect(creditCardFormService.getCreditCard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(creditCardService.update).toHaveBeenCalledWith(expect.objectContaining(creditCard));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICreditCard>>();
      const creditCard = { id: 123 };
      jest.spyOn(creditCardFormService, 'getCreditCard').mockReturnValue({ id: null });
      jest.spyOn(creditCardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCard: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: creditCard }));
      saveSubject.complete();

      // THEN
      expect(creditCardFormService.getCreditCard).toHaveBeenCalled();
      expect(creditCardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICreditCard>>();
      const creditCard = { id: 123 };
      jest.spyOn(creditCardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(creditCardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
