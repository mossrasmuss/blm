import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMedia } from 'app/shared/model/media.model';
import { MediaService } from './media.service';

@Component({
  templateUrl: './media-delete-dialog.component.html',
})
export class MediaDeleteDialogComponent {
  media?: IMedia;

  constructor(protected mediaService: MediaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mediaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('mediaListModification');
      this.activeModal.close();
    });
  }
}
