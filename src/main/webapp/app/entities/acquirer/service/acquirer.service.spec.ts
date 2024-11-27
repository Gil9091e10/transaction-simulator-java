import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAcquirer } from '../acquirer.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../acquirer.test-samples';

import { AcquirerService } from './acquirer.service';

const requireRestSample: IAcquirer = {
  ...sampleWithRequiredData,
};

describe('Acquirer Service', () => {
  let service: AcquirerService;
  let httpMock: HttpTestingController;
  let expectedResult: IAcquirer | IAcquirer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AcquirerService);
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

    it('should create a Acquirer', () => {
      const acquirer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(acquirer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Acquirer', () => {
      const acquirer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(acquirer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Acquirer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Acquirer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Acquirer', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAcquirerToCollectionIfMissing', () => {
      it('should add a Acquirer to an empty array', () => {
        const acquirer: IAcquirer = sampleWithRequiredData;
        expectedResult = service.addAcquirerToCollectionIfMissing([], acquirer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acquirer);
      });

      it('should not add a Acquirer to an array that contains it', () => {
        const acquirer: IAcquirer = sampleWithRequiredData;
        const acquirerCollection: IAcquirer[] = [
          {
            ...acquirer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAcquirerToCollectionIfMissing(acquirerCollection, acquirer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Acquirer to an array that doesn't contain it", () => {
        const acquirer: IAcquirer = sampleWithRequiredData;
        const acquirerCollection: IAcquirer[] = [sampleWithPartialData];
        expectedResult = service.addAcquirerToCollectionIfMissing(acquirerCollection, acquirer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acquirer);
      });

      it('should add only unique Acquirer to an array', () => {
        const acquirerArray: IAcquirer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const acquirerCollection: IAcquirer[] = [sampleWithRequiredData];
        expectedResult = service.addAcquirerToCollectionIfMissing(acquirerCollection, ...acquirerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const acquirer: IAcquirer = sampleWithRequiredData;
        const acquirer2: IAcquirer = sampleWithPartialData;
        expectedResult = service.addAcquirerToCollectionIfMissing([], acquirer, acquirer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acquirer);
        expect(expectedResult).toContain(acquirer2);
      });

      it('should accept null and undefined values', () => {
        const acquirer: IAcquirer = sampleWithRequiredData;
        expectedResult = service.addAcquirerToCollectionIfMissing([], null, acquirer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acquirer);
      });

      it('should return initial array if no Acquirer is added', () => {
        const acquirerCollection: IAcquirer[] = [sampleWithRequiredData];
        expectedResult = service.addAcquirerToCollectionIfMissing(acquirerCollection, undefined, null);
        expect(expectedResult).toEqual(acquirerCollection);
      });
    });

    describe('compareAcquirer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAcquirer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAcquirer(entity1, entity2);
        const compareResult2 = service.compareAcquirer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAcquirer(entity1, entity2);
        const compareResult2 = service.compareAcquirer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAcquirer(entity1, entity2);
        const compareResult2 = service.compareAcquirer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
