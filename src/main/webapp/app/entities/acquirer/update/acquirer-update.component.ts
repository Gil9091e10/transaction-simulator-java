import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAcquirer } from '../acquirer.model';
import { AcquirerService } from '../service/acquirer.service';
import { AcquirerFormGroup, AcquirerFormService } from './acquirer-form.service';

@Component({
  standalone: true,
  selector: 'stp-acquirer-update',
  templateUrl: './acquirer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AcquirerUpdateComponent implements OnInit {
  isSaving = false;
  acquirer: IAcquirer | null = null;

  protected acquirerService = inject(AcquirerService);
  protected acquirerFormService = inject(AcquirerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AcquirerFormGroup = this.acquirerFormService.createAcquirerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acquirer }) => {
      this.acquirer = acquirer;
      if (acquirer) {
        this.updateForm(acquirer);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const acquirer = this.acquirerFormService.getAcquirer(this.editForm);
    if (acquirer.id !== null) {
      this.subscribeToSaveResponse(this.acquirerService.update(acquirer));
    } else {
      this.subscribeToSaveResponse(this.acquirerService.create(acquirer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAcquirer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(acquirer: IAcquirer): void {
    this.acquirer = acquirer;
    this.acquirerFormService.resetForm(this.editForm, acquirer);
  }
}
