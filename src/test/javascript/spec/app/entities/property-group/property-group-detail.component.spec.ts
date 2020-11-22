import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlmTestModule } from '../../../test.module';
import { PropertyGroupDetailComponent } from 'app/entities/property-group/property-group-detail.component';
import { PropertyGroup } from 'app/shared/model/property-group.model';

describe('Component Tests', () => {
  describe('PropertyGroup Management Detail Component', () => {
    let comp: PropertyGroupDetailComponent;
    let fixture: ComponentFixture<PropertyGroupDetailComponent>;
    const route = ({ data: of({ propertyGroup: new PropertyGroup(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [PropertyGroupDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PropertyGroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PropertyGroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load propertyGroup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.propertyGroup).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
