jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MessageTypeIndicatorService } from '../service/message-type-indicator.service';

import { MessageTypeIndicatorDeleteDialogComponent } from './message-type-indicator-delete-dialog.component';

describe('MessageTypeIndicator Management Delete Component', () => {
  let comp: MessageTypeIndicatorDeleteDialogComponent;
  let fixture: ComponentFixture<MessageTypeIndicatorDeleteDialogComponent>;
  let service: MessageTypeIndicatorService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MessageTypeIndicatorDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(MessageTypeIndicatorDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MessageTypeIndicatorDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MessageTypeIndicatorService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
