import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAcquirer } from 'app/entities/acquirer/acquirer.model';
import { AcquirerService } from 'app/entities/acquirer/service/acquirer.service';
import { IMessageTypeIndicator } from 'app/entities/message-type-indicator/message-type-indicator.model';
import { MessageTypeIndicatorService } from 'app/entities/message-type-indicator/service/message-type-indicator.service';
import { IMessageIsoConfig } from '../message-iso-config.model';
import { MessageIsoConfigService } from '../service/message-iso-config.service';
import { MessageIsoConfigFormService } from './message-iso-config-form.service';

import { MessageIsoConfigUpdateComponent } from './message-iso-config-update.component';

describe('MessageIsoConfig Management Update Component', () => {
  let comp: MessageIsoConfigUpdateComponent;
  let fixture: ComponentFixture<MessageIsoConfigUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let messageIsoConfigFormService: MessageIsoConfigFormService;
  let messageIsoConfigService: MessageIsoConfigService;
  let acquirerService: AcquirerService;
  let messageTypeIndicatorService: MessageTypeIndicatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MessageIsoConfigUpdateComponent],
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
      .overrideTemplate(MessageIsoConfigUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MessageIsoConfigUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    messageIsoConfigFormService = TestBed.inject(MessageIsoConfigFormService);
    messageIsoConfigService = TestBed.inject(MessageIsoConfigService);
    acquirerService = TestBed.inject(AcquirerService);
    messageTypeIndicatorService = TestBed.inject(MessageTypeIndicatorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Acquirer query and add missing value', () => {
      const messageIsoConfig: IMessageIsoConfig = { id: 456 };
      const acquirer: IAcquirer = { id: 11902 };
      messageIsoConfig.acquirer = acquirer;

      const acquirerCollection: IAcquirer[] = [{ id: 28805 }];
      jest.spyOn(acquirerService, 'query').mockReturnValue(of(new HttpResponse({ body: acquirerCollection })));
      const additionalAcquirers = [acquirer];
      const expectedCollection: IAcquirer[] = [...additionalAcquirers, ...acquirerCollection];
      jest.spyOn(acquirerService, 'addAcquirerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ messageIsoConfig });
      comp.ngOnInit();

      expect(acquirerService.query).toHaveBeenCalled();
      expect(acquirerService.addAcquirerToCollectionIfMissing).toHaveBeenCalledWith(
        acquirerCollection,
        ...additionalAcquirers.map(expect.objectContaining),
      );
      expect(comp.acquirersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MessageTypeIndicator query and add missing value', () => {
      const messageIsoConfig: IMessageIsoConfig = { id: 456 };
      const messageTypeIndicator: IMessageTypeIndicator = { id: 28325 };
      messageIsoConfig.messageTypeIndicator = messageTypeIndicator;

      const messageTypeIndicatorCollection: IMessageTypeIndicator[] = [{ id: 31806 }];
      jest.spyOn(messageTypeIndicatorService, 'query').mockReturnValue(of(new HttpResponse({ body: messageTypeIndicatorCollection })));
      const additionalMessageTypeIndicators = [messageTypeIndicator];
      const expectedCollection: IMessageTypeIndicator[] = [...additionalMessageTypeIndicators, ...messageTypeIndicatorCollection];
      jest.spyOn(messageTypeIndicatorService, 'addMessageTypeIndicatorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ messageIsoConfig });
      comp.ngOnInit();

      expect(messageTypeIndicatorService.query).toHaveBeenCalled();
      expect(messageTypeIndicatorService.addMessageTypeIndicatorToCollectionIfMissing).toHaveBeenCalledWith(
        messageTypeIndicatorCollection,
        ...additionalMessageTypeIndicators.map(expect.objectContaining),
      );
      expect(comp.messageTypeIndicatorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const messageIsoConfig: IMessageIsoConfig = { id: 456 };
      const acquirer: IAcquirer = { id: 8328 };
      messageIsoConfig.acquirer = acquirer;
      const messageTypeIndicator: IMessageTypeIndicator = { id: 4245 };
      messageIsoConfig.messageTypeIndicator = messageTypeIndicator;

      activatedRoute.data = of({ messageIsoConfig });
      comp.ngOnInit();

      expect(comp.acquirersSharedCollection).toContain(acquirer);
      expect(comp.messageTypeIndicatorsSharedCollection).toContain(messageTypeIndicator);
      expect(comp.messageIsoConfig).toEqual(messageIsoConfig);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessageIsoConfig>>();
      const messageIsoConfig = { id: 123 };
      jest.spyOn(messageIsoConfigFormService, 'getMessageIsoConfig').mockReturnValue(messageIsoConfig);
      jest.spyOn(messageIsoConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ messageIsoConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: messageIsoConfig }));
      saveSubject.complete();

      // THEN
      expect(messageIsoConfigFormService.getMessageIsoConfig).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(messageIsoConfigService.update).toHaveBeenCalledWith(expect.objectContaining(messageIsoConfig));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessageIsoConfig>>();
      const messageIsoConfig = { id: 123 };
      jest.spyOn(messageIsoConfigFormService, 'getMessageIsoConfig').mockReturnValue({ id: null });
      jest.spyOn(messageIsoConfigService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ messageIsoConfig: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: messageIsoConfig }));
      saveSubject.complete();

      // THEN
      expect(messageIsoConfigFormService.getMessageIsoConfig).toHaveBeenCalled();
      expect(messageIsoConfigService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessageIsoConfig>>();
      const messageIsoConfig = { id: 123 };
      jest.spyOn(messageIsoConfigService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ messageIsoConfig });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(messageIsoConfigService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAcquirer', () => {
      it('Should forward to acquirerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(acquirerService, 'compareAcquirer');
        comp.compareAcquirer(entity, entity2);
        expect(acquirerService.compareAcquirer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMessageTypeIndicator', () => {
      it('Should forward to messageTypeIndicatorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(messageTypeIndicatorService, 'compareMessageTypeIndicator');
        comp.compareMessageTypeIndicator(entity, entity2);
        expect(messageTypeIndicatorService.compareMessageTypeIndicator).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
