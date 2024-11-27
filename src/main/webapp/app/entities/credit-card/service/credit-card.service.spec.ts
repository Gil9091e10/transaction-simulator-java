import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICreditCard } from '../credit-card.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../credit-card.test-samples';

import { CreditCardService } from './credit-card.service';

const requireRestSample: ICreditCard = {
  ...sampleWithRequiredData,
};

describe('CreditCard Service', () => {
  let service: CreditCardService;
  let httpMock: HttpTestingController;
  let expectedResult: ICreditCard | ICreditCard[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CreditCardService);
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

    it('should create a CreditCard', () => {
      const creditCard = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(creditCard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CreditCard', () => {
      const creditCard = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(creditCard).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CreditCard', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CreditCard', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CreditCard', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCreditCardToCollectionIfMissing', () => {
      it('should add a CreditCard to an empty array', () => {
        const creditCard: ICreditCard = sampleWithRequiredData;
        expectedResult = service.addCreditCardToCollectionIfMissing([], creditCard);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(creditCard);
      });

      it('should not add a CreditCard to an array that contains it', () => {
        const creditCard: ICreditCard = sampleWithRequiredData;
        const creditCardCollection: ICreditCard[] = [
          {
            ...creditCard,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCreditCardToCollectionIfMissing(creditCardCollection, creditCard);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CreditCard to an array that doesn't contain it", () => {
        const creditCard: ICreditCard = sampleWithRequiredData;
        const creditCardCollection: ICreditCard[] = [sampleWithPartialData];
        expectedResult = service.addCreditCardToCollectionIfMissing(creditCardCollection, creditCard);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(creditCard);
      });

      it('should add only unique CreditCard to an array', () => {
        const creditCardArray: ICreditCard[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const creditCardCollection: ICreditCard[] = [sampleWithRequiredData];
        expectedResult = service.addCreditCardToCollectionIfMissing(creditCardCollection, ...creditCardArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const creditCard: ICreditCard = sampleWithRequiredData;
        const creditCard2: ICreditCard = sampleWithPartialData;
        expectedResult = service.addCreditCardToCollectionIfMissing([], creditCard, creditCard2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(creditCard);
        expect(expectedResult).toContain(creditCard2);
      });

      it('should accept null and undefined values', () => {
        const creditCard: ICreditCard = sampleWithRequiredData;
        expectedResult = service.addCreditCardToCollectionIfMissing([], null, creditCard, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(creditCard);
      });

      it('should return initial array if no CreditCard is added', () => {
        const creditCardCollection: ICreditCard[] = [sampleWithRequiredData];
        expectedResult = service.addCreditCardToCollectionIfMissing(creditCardCollection, undefined, null);
        expect(expectedResult).toEqual(creditCardCollection);
      });
    });

    describe('compareCreditCard', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCreditCard(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCreditCard(entity1, entity2);
        const compareResult2 = service.compareCreditCard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCreditCard(entity1, entity2);
        const compareResult2 = service.compareCreditCard(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCreditCard(entity1, entity2);
        const compareResult2 = service.compareCreditCard(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
