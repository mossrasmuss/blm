import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BlmTestModule } from '../../../test.module';
import { PropertyGroupComponent } from 'app/entities/property-group/property-group.component';
import { PropertyGroupService } from 'app/entities/property-group/property-group.service';
import { PropertyGroup } from 'app/shared/model/property-group.model';

describe('Component Tests', () => {
  describe('PropertyGroup Management Component', () => {
    let comp: PropertyGroupComponent;
    let fixture: ComponentFixture<PropertyGroupComponent>;
    let service: PropertyGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [PropertyGroupComponent],
      })
        .overrideTemplate(PropertyGroupComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PropertyGroupComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PropertyGroupService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PropertyGroup(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.propertyGroups && comp.propertyGroups[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
