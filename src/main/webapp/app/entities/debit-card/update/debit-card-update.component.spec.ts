import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DebitCardService } from '../service/debit-card.service';
import { IDebitCard } from '../debit-card.model';
import { DebitCardFormService } from './debit-card-form.service';

import { DebitCardUpdateComponent } from './debit-card-update.component';

describe('DebitCard Management Update Component', () => {
  let comp: DebitCardUpdateComponent;
  let fixture: ComponentFixture<DebitCardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let debitCardFormService: DebitCardFormService;
  let debitCardService: DebitCardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DebitCardUpdateComponent],
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
      .overrideTemplate(DebitCardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DebitCardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    debitCardFormService = TestBed.inject(DebitCardFormService);
    debitCardService = TestBed.inject(DebitCardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const debitCard: IDebitCard = { id: 456 };

      activatedRoute.data = of({ debitCard });
      comp.ngOnInit();

      expect(comp.debitCard).toEqual(debitCard);
    });
  });

  describe('save', () => {
    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDebitCard>>();
      const debitCard = { id: 123 };
      jest.spyOn(debitCardFormService, 'getDebitCard').mockReturnValue({ id: null });
      jest.spyOn(debitCardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ debitCard: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: debitCard }));
      saveSubject.complete();

      // THEN
      expect(debitCardFormService.getDebitCard).toHaveBeenCalled();
      expect(debitCardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });
  });
});
