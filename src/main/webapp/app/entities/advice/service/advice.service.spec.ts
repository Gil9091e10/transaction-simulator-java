import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAdvice } from '../advice.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../advice.test-samples';

import { AdviceService } from './advice.service';

const requireRestSample: IAdvice = {
  ...sampleWithRequiredData,
};

describe('Advice Service', () => {
  let service: AdviceService;
  let httpMock: HttpTestingController;
  let expectedResult: IAdvice | IAdvice[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AdviceService);
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

    it('should create a Advice', () => {
      const advice = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(advice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Advice', () => {
      const advice = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(advice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Advice', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Advice', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Advice', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAdviceToCollectionIfMissing', () => {
      it('should add a Advice to an empty array', () => {
        const advice: IAdvice = sampleWithRequiredData;
        expectedResult = service.addAdviceToCollectionIfMissing([], advice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(advice);
      });

      it('should not add a Advice to an array that contains it', () => {
        const advice: IAdvice = sampleWithRequiredData;
        const adviceCollection: IAdvice[] = [
          {
            ...advice,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAdviceToCollectionIfMissing(adviceCollection, advice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Advice to an array that doesn't contain it", () => {
        const advice: IAdvice = sampleWithRequiredData;
        const adviceCollection: IAdvice[] = [sampleWithPartialData];
        expectedResult = service.addAdviceToCollectionIfMissing(adviceCollection, advice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(advice);
      });

      it('should add only unique Advice to an array', () => {
        const adviceArray: IAdvice[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const adviceCollection: IAdvice[] = [sampleWithRequiredData];
        expectedResult = service.addAdviceToCollectionIfMissing(adviceCollection, ...adviceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const advice: IAdvice = sampleWithRequiredData;
        const advice2: IAdvice = sampleWithPartialData;
        expectedResult = service.addAdviceToCollectionIfMissing([], advice, advice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(advice);
        expect(expectedResult).toContain(advice2);
      });

      it('should accept null and undefined values', () => {
        const advice: IAdvice = sampleWithRequiredData;
        expectedResult = service.addAdviceToCollectionIfMissing([], null, advice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(advice);
      });

      it('should return initial array if no Advice is added', () => {
        const adviceCollection: IAdvice[] = [sampleWithRequiredData];
        expectedResult = service.addAdviceToCollectionIfMissing(adviceCollection, undefined, null);
        expect(expectedResult).toEqual(adviceCollection);
      });
    });

    describe('compareAdvice', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAdvice(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAdvice(entity1, entity2);
        const compareResult2 = service.compareAdvice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAdvice(entity1, entity2);
        const compareResult2 = service.compareAdvice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAdvice(entity1, entity2);
        const compareResult2 = service.compareAdvice(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
