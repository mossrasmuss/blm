import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BlmTestModule } from '../../../test.module';
import { MediaComponent } from 'app/entities/media/media.component';
import { MediaService } from 'app/entities/media/media.service';
import { Media } from 'app/shared/model/media.model';

describe('Component Tests', () => {
  describe('Media Management Component', () => {
    let comp: MediaComponent;
    let fixture: ComponentFixture<MediaComponent>;
    let service: MediaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlmTestModule],
        declarations: [MediaComponent],
      })
        .overrideTemplate(MediaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MediaComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MediaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Media(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.media && comp.media[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
