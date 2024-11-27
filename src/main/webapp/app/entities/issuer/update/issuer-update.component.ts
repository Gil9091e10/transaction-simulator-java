import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IIssuer } from '../issuer.model';
import { IssuerService } from '../service/issuer.service';
import { IssuerFormGroup, IssuerFormService } from './issuer-form.service';

@Component({
  standalone: true,
  selector: 'stp-issuer-update',
  templateUrl: './issuer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class IssuerUpdateComponent implements OnInit {
  isSaving = false;
  issuer: IIssuer | null = null;

  protected issuerService = inject(IssuerService);
  protected issuerFormService = inject(IssuerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IssuerFormGroup = this.issuerFormService.createIssuerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ issuer }) => {
      this.issuer = issuer;
      if (issuer) {
        this.updateForm(issuer);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const issuer = this.issuerFormService.getIssuer(this.editForm);
    if (issuer.id !== null) {
      this.subscribeToSaveResponse(this.issuerService.update(issuer));
    } else {
      this.subscribeToSaveResponse(this.issuerService.create(issuer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIssuer>>): void {
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

  protected updateForm(issuer: IIssuer): void {
    this.issuer = issuer;
    this.issuerFormService.resetForm(this.editForm, issuer);
  }
}
