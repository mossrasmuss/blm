import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SalesPropertyService } from 'app/entities/sales-property/sales-property.service';
import { ISalesProperty, SalesProperty } from 'app/shared/model/sales-property.model';
import { SalesType } from 'app/shared/model/enumerations/sales-type.model';
import { SalesStatus } from 'app/shared/model/enumerations/sales-status.model';

describe('Service Tests', () => {
  describe('SalesProperty Service', () => {
    let injector: TestBed;
    let service: SalesPropertyService;
    let httpMock: HttpTestingController;
    let elemDefault: ISalesProperty;
    let expectedResult: ISalesProperty | ISalesProperty[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SalesPropertyService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new SalesProperty(0, currentDate, currentDate, SalesType.SALES, SalesStatus.SOLD, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            datePosted: currentDate.format(DATE_TIME_FORMAT),
            expiryDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SalesProperty', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            datePosted: currentDate.format(DATE_TIME_FORMAT),
            expiryDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePosted: currentDate,
            expiryDate: currentDate,
          },
          returnedFromService
        );

        service.create(new SalesProperty()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SalesProperty', () => {
        const returnedFromService = Object.assign(
          {
            datePosted: currentDate.format(DATE_TIME_FORMAT),
            expiryDate: currentDate.format(DATE_TIME_FORMAT),
            type: 'BBBBBB',
            status: 'BBBBBB',
            defaultPrice: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePosted: currentDate,
            expiryDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SalesProperty', () => {
        const returnedFromService = Object.assign(
          {
            datePosted: currentDate.format(DATE_TIME_FORMAT),
            expiryDate: currentDate.format(DATE_TIME_FORMAT),
            type: 'BBBBBB',
            status: 'BBBBBB',
            defaultPrice: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            datePosted: currentDate,
            expiryDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SalesProperty', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
