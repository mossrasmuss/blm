import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlmTestModule } from '../../../test.module';
import { AttributeDetailComponent } from 'app/entities/attribute/attribute-detail.component';
import { Attribute } from 'app/shared/model/attribute.model';

describe('Component Tests', () => {
  describe('Attribute Management Detail Component', () => {
    let comp: AttributeDetailComponent;
    let fixture: ComponentFixture<AttributeDetailComponent>;
    const route = ({ data: of({ attribute: new Attribute(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [AttributeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AttributeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttributeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load attribute on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.attribute).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
