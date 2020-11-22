import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BlmTestModule } from '../../../test.module';
import { PropertyGroupUpdateComponent } from 'app/entities/property-group/property-group-update.component';
import { PropertyGroupService } from 'app/entities/property-group/property-group.service';
import { PropertyGroup } from 'app/shared/model/property-group.model';

describe('Component Tests', () => {
  describe('PropertyGroup Management Update Component', () => {
    let comp: PropertyGroupUpdateComponent;
    let fixture: ComponentFixture<PropertyGroupUpdateComponent>;
    let service: PropertyGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [PropertyGroupUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PropertyGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PropertyGroupUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PropertyGroupService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PropertyGroup(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new PropertyGroup();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
