import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BlmTestModule } from '../../../test.module';
import { AttributeValueComponent } from 'app/entities/attribute-value/attribute-value.component';
import { AttributeValueService } from 'app/entities/attribute-value/attribute-value.service';
import { AttributeValue } from 'app/shared/model/attribute-value.model';

describe('Component Tests', () => {
  describe('AttributeValue Management Component', () => {
    let comp: AttributeValueComponent;
    let fixture: ComponentFixture<AttributeValueComponent>;
    let service: AttributeValueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [AttributeValueComponent],
      })
        .overrideTemplate(AttributeValueComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttributeValueComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttributeValueService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new AttributeValue(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.attributeValues && comp.attributeValues[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
