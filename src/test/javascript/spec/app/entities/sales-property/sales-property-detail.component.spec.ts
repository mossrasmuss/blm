import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlmTestModule } from '../../../test.module';
import { SalesPropertyDetailComponent } from 'app/entities/sales-property/sales-property-detail.component';
import { SalesProperty } from 'app/shared/model/sales-property.model';

describe('Component Tests', () => {
  describe('SalesProperty Management Detail Component', () => {
    let comp: SalesPropertyDetailComponent;
    let fixture: ComponentFixture<SalesPropertyDetailComponent>;
    const route = ({ data: of({ salesProperty: new SalesProperty(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [SalesPropertyDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SalesPropertyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SalesPropertyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load salesProperty on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.salesProperty).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
