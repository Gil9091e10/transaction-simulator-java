import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDebitCard } from '../debit-card.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../debit-card.test-samples';

import { DebitCardService } from './debit-card.service';

const requireRestSample: IDebitCard = {
  ...sampleWithRequiredData,
};

describe('DebitCard Service', () => {
  let service: DebitCardService;
  let httpMock: HttpTestingController;
  let expectedResult: IDebitCard | IDebitCard[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DebitCardService);
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

    it('should create a DebitCard', () => {
      const debitCard = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(debitCard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DebitCard', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DebitCard', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDebitCardToCollectionIfMissing', () => {
      it('should add a DebitCard to an empty array', () => {
        const debitCard: IDebitCard = sampleWithRequiredData;
        expectedResult = service.addDebitCardToCollectionIfMissing([], debitCard);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(debitCard);
      });

      it('should not add a DebitCard to an array that contains it', () => {
        const debitCard: IDebitCard = sampleWithRequiredData;
        const debitCardCollection: IDebitCard[] = [
          {
            ...debitCard,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDebitCardToCollectionIfMissing(debitCardCollection, debitCard);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DebitCard to an array that doesn't contain it", () => {
        const debitCard: IDebitCard = sampleWithRequiredData;
        const debitCardCollection: IDebitCard[] = [sampleWithPartialData];
        expectedResult = service.addDebitCardToCollectionIfMissing(debitCardCollection, debitCard);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(debitCard);
      });

      it('should add only unique DebitCard to an array', () => {
        const debitCardArray: IDebitCard[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const debitCardCollection: IDebitCard[] = [sampleWithRequiredData];
        expectedResult = service.addDebitCardToCollectionIfMissing(debitCardCollection, ...debitCardArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const debitCard: IDebitCard = sampleWithRequiredData;
        const debitCard2: IDebitCard = sampleWithPartialData;
        expectedResult = service.addDebitCardToCollectionIfMissing([], debitCard, debitCard2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(debitCard);
        expect(expectedResult).toContain(debitCard2);
      });

      it('should accept null and undefined values', () => {
        const debitCard: IDebitCard = sampleWithRequiredData;
        expectedResult = service.addDebitCardToCollectionIfMissing([], null, debitCard, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(debitCard);
      });

      it('should return initial array if no DebitCard is added', () => {
        const debitCardCollection: IDebitCard[] = [sampleWithRequiredData];
        expectedResult = service.addDebitCardToCollectionIfMissing(debitCardCollection, undefined, null);
        expect(expectedResult).toEqual(debitCardCollection);
      });
    });

    describe('compareDebitCard', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDebitCard(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDebitCard(entity1, entity2);
        const compareResult2 = service.compareDebitCard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDebitCard(entity1, entity2);
        const compareResult2 = service.compareDebitCard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDebitCard(entity1, entity2);
        const compareResult2 = service.compareDebitCard(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
