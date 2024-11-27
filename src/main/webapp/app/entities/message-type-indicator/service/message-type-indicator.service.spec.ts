import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMessageTypeIndicator } from '../message-type-indicator.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../message-type-indicator.test-samples';

import { MessageTypeIndicatorService } from './message-type-indicator.service';

const requireRestSample: IMessageTypeIndicator = {
  ...sampleWithRequiredData,
};

describe('MessageTypeIndicator Service', () => {
  let service: MessageTypeIndicatorService;
  let httpMock: HttpTestingController;
  let expectedResult: IMessageTypeIndicator | IMessageTypeIndicator[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MessageTypeIndicatorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MessageTypeIndicator', () => {
      const messageTypeIndicator = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(messageTypeIndicator).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MessageTypeIndicator', () => {
      const messageTypeIndicator = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(messageTypeIndicator).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MessageTypeIndicator', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MessageTypeIndicator', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MessageTypeIndicator', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMessageTypeIndicatorToCollectionIfMissing', () => {
      it('should add a MessageTypeIndicator to an empty array', () => {
        const messageTypeIndicator: IMessageTypeIndicator = sampleWithRequiredData;
        expectedResult = service.addMessageTypeIndicatorToCollectionIfMissing([], messageTypeIndicator);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(messageTypeIndicator);
      });

      it('should not add a MessageTypeIndicator to an array that contains it', () => {
        const messageTypeIndicator: IMessageTypeIndicator = sampleWithRequiredData;
        const messageTypeIndicatorCollection: IMessageTypeIndicator[] = [
          {
            ...messageTypeIndicator,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMessageTypeIndicatorToCollectionIfMissing(messageTypeIndicatorCollection, messageTypeIndicator);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MessageTypeIndicator to an array that doesn't contain it", () => {
        const messageTypeIndicator: IMessageTypeIndicator = sampleWithRequiredData;
        const messageTypeIndicatorCollection: IMessageTypeIndicator[] = [sampleWithPartialData];
        expectedResult = service.addMessageTypeIndicatorToCollectionIfMissing(messageTypeIndicatorCollection, messageTypeIndicator);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(messageTypeIndicator);
      });

      it('should add only unique MessageTypeIndicator to an array', () => {
        const messageTypeIndicatorArray: IMessageTypeIndicator[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const messageTypeIndicatorCollection: IMessageTypeIndicator[] = [sampleWithRequiredData];
        expectedResult = service.addMessageTypeIndicatorToCollectionIfMissing(messageTypeIndicatorCollection, ...messageTypeIndicatorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const messageTypeIndicator: IMessageTypeIndicator = sampleWithRequiredData;
        const messageTypeIndicator2: IMessageTypeIndicator = sampleWithPartialData;
        expectedResult = service.addMessageTypeIndicatorToCollectionIfMissing([], messageTypeIndicator, messageTypeIndicator2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(messageTypeIndicator);
        expect(expectedResult).toContain(messageTypeIndicator2);
      });

      it('should accept null and undefined values', () => {
        const messageTypeIndicator: IMessageTypeIndicator = sampleWithRequiredData;
        expectedResult = service.addMessageTypeIndicatorToCollectionIfMissing([], null, messageTypeIndicator, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(messageTypeIndicator);
      });

      it('should return initial array if no MessageTypeIndicator is added', () => {
        const messageTypeIndicatorCollection: IMessageTypeIndicator[] = [sampleWithRequiredData];
        expectedResult = service.addMessageTypeIndicatorToCollectionIfMissing(messageTypeIndicatorCollection, undefined, null);
        expect(expectedResult).toEqual(messageTypeIndicatorCollection);
      });
    });

    describe('compareMessageTypeIndicator', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMessageTypeIndicator(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMessageTypeIndicator(entity1, entity2);
        const compareResult2 = service.compareMessageTypeIndicator(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMessageTypeIndicator(entity1, entity2);
        const compareResult2 = service.compareMessageTypeIndicator(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMessageTypeIndicator(entity1, entity2);
        const compareResult2 = service.compareMessageTypeIndicator(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
