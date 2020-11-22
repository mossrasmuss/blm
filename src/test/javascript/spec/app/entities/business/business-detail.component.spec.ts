import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlmTestModule } from '../../../test.module';
import { BusinessDetailComponent } from 'app/entities/business/business-detail.component';
import { Business } from 'app/shared/model/business.model';

describe('Component Tests', () => {
  describe('Business Management Detail Component', () => {
    let comp: BusinessDetailComponent;
    let fixture: ComponentFixture<BusinessDetailComponent>;
    const route = ({ data: of({ business: new Business(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [BusinessDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(BusinessDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BusinessDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load business on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.business).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
