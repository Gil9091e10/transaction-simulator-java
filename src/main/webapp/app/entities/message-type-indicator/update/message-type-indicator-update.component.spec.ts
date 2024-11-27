import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MessageTypeIndicatorService } from '../service/message-type-indicator.service';
import { IMessageTypeIndicator } from '../message-type-indicator.model';
import { MessageTypeIndicatorFormService } from './message-type-indicator-form.service';

import { MessageTypeIndicatorUpdateComponent } from './message-type-indicator-update.component';

describe('MessageTypeIndicator Management Update Component', () => {
  let comp: MessageTypeIndicatorUpdateComponent;
  let fixture: ComponentFixture<MessageTypeIndicatorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let messageTypeIndicatorFormService: MessageTypeIndicatorFormService;
  let messageTypeIndicatorService: MessageTypeIndicatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MessageTypeIndicatorUpdateComponent],
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
      .overrideTemplate(MessageTypeIndicatorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MessageTypeIndicatorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    messageTypeIndicatorFormService = TestBed.inject(MessageTypeIndicatorFormService);
    messageTypeIndicatorService = TestBed.inject(MessageTypeIndicatorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const messageTypeIndicator: IMessageTypeIndicator = { id: 456 };

      activatedRoute.data = of({ messageTypeIndicator });
      comp.ngOnInit();

      expect(comp.messageTypeIndicator).toEqual(messageTypeIndicator);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessageTypeIndicator>>();
      const messageTypeIndicator = { id: 123 };
      jest.spyOn(messageTypeIndicatorFormService, 'getMessageTypeIndicator').mockReturnValue(messageTypeIndicator);
      jest.spyOn(messageTypeIndicatorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ messageTypeIndicator });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: messageTypeIndicator }));
      saveSubject.complete();

      // THEN
      expect(messageTypeIndicatorFormService.getMessageTypeIndicator).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(messageTypeIndicatorService.update).toHaveBeenCalledWith(expect.objectContaining(messageTypeIndicator));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessageTypeIndicator>>();
      const messageTypeIndicator = { id: 123 };
      jest.spyOn(messageTypeIndicatorFormService, 'getMessageTypeIndicator').mockReturnValue({ id: null });
      jest.spyOn(messageTypeIndicatorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ messageTypeIndicator: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: messageTypeIndicator }));
      saveSubject.complete();

      // THEN
      expect(messageTypeIndicatorFormService.getMessageTypeIndicator).toHaveBeenCalled();
      expect(messageTypeIndicatorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessageTypeIndicator>>();
      const messageTypeIndicator = { id: 123 };
      jest.spyOn(messageTypeIndicatorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ messageTypeIndicator });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(messageTypeIndicatorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
