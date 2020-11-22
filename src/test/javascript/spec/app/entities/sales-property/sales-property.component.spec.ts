import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BlmTestModule } from '../../../test.module';
import { SalesPropertyComponent } from 'app/entities/sales-property/sales-property.component';
import { SalesPropertyService } from 'app/entities/sales-property/sales-property.service';
import { SalesProperty } from 'app/shared/model/sales-property.model';

describe('Component Tests', () => {
  describe('SalesProperty Management Component', () => {
    let comp: SalesPropertyComponent;
    let fixture: ComponentFixture<SalesPropertyComponent>;
    let service: SalesPropertyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [SalesPropertyComponent],
      })
        .overrideTemplate(SalesPropertyComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SalesPropertyComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SalesPropertyService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SalesProperty(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.salesProperties && comp.salesProperties[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
