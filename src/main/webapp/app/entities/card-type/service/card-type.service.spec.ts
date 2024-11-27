import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICardType } from '../card-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../card-type.test-samples';

import { CardTypeService } from './card-type.service';

const requireRestSample: ICardType = {
  ...sampleWithRequiredData,
};

describe('CardType Service', () => {
  let service: CardTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICardType | ICardType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CardTypeService);
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

    it('should create a CardType', () => {
      const cardType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cardType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CardType', () => {
      const cardType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cardType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CardType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CardType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CardType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCardTypeToCollectionIfMissing', () => {
      it('should add a CardType to an empty array', () => {
        const cardType: ICardType = sampleWithRequiredData;
        expectedResult = service.addCardTypeToCollectionIfMissing([], cardType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardType);
      });

      it('should not add a CardType to an array that contains it', () => {
        const cardType: ICardType = sampleWithRequiredData;
        const cardTypeCollection: ICardType[] = [
          {
            ...cardType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCardTypeToCollectionIfMissing(cardTypeCollection, cardType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CardType to an array that doesn't contain it", () => {
        const cardType: ICardType = sampleWithRequiredData;
        const cardTypeCollection: ICardType[] = [sampleWithPartialData];
        expectedResult = service.addCardTypeToCollectionIfMissing(cardTypeCollection, cardType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardType);
      });

      it('should add only unique CardType to an array', () => {
        const cardTypeArray: ICardType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cardTypeCollection: ICardType[] = [sampleWithRequiredData];
        expectedResult = service.addCardTypeToCollectionIfMissing(cardTypeCollection, ...cardTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cardType: ICardType = sampleWithRequiredData;
        const cardType2: ICardType = sampleWithPartialData;
        expectedResult = service.addCardTypeToCollectionIfMissing([], cardType, cardType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cardType);
        expect(expectedResult).toContain(cardType2);
      });

      it('should accept null and undefined values', () => {
        const cardType: ICardType = sampleWithRequiredData;
        expectedResult = service.addCardTypeToCollectionIfMissing([], null, cardType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cardType);
      });

      it('should return initial array if no CardType is added', () => {
        const cardTypeCollection: ICardType[] = [sampleWithRequiredData];
        expectedResult = service.addCardTypeToCollectionIfMissing(cardTypeCollection, undefined, null);
        expect(expectedResult).toEqual(cardTypeCollection);
      });
    });

    describe('compareCardType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCardType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCardType(entity1, entity2);
        const compareResult2 = service.compareCardType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCardType(entity1, entity2);
        const compareResult2 = service.compareCardType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCardType(entity1, entity2);
        const compareResult2 = service.compareCardType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
