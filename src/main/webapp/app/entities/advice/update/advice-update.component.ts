import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { IAcquirer } from 'app/entities/acquirer/acquirer.model';
import { AcquirerService } from 'app/entities/acquirer/service/acquirer.service';
import { AdviceService } from '../service/advice.service';
import { IAdvice } from '../advice.model';
import { AdviceFormGroup, AdviceFormService } from './advice-form.service';

@Component({
  standalone: true,
  selector: 'stp-advice-update',
  templateUrl: './advice-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AdviceUpdateComponent implements OnInit {
  isSaving = false;
  advice: IAdvice | null = null;

  merchantsSharedCollection: IMerchant[] = [];
  acquirersSharedCollection: IAcquirer[] = [];

  protected adviceService = inject(AdviceService);
  protected adviceFormService = inject(AdviceFormService);
  protected merchantService = inject(MerchantService);
  protected acquirerService = inject(AcquirerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AdviceFormGroup = this.adviceFormService.createAdviceFormGroup();

  compareMerchant = (o1: IMerchant | null, o2: IMerchant | null): boolean => this.merchantService.compareMerchant(o1, o2);

  compareAcquirer = (o1: IAcquirer | null, o2: IAcquirer | null): boolean => this.acquirerService.compareAcquirer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ advice }) => {
      this.advice = advice;
      if (advice) {
        this.updateForm(advice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const advice = this.adviceFormService.getAdvice(this.editForm);
    if (advice.id !== null) {
      this.subscribeToSaveResponse(this.adviceService.update(advice));
    } else {
      this.subscribeToSaveResponse(this.adviceService.create(advice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdvice>>): void {
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

  protected updateForm(advice: IAdvice): void {
    this.advice = advice;
    this.adviceFormService.resetForm(this.editForm, advice);

    this.merchantsSharedCollection = this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(
      this.merchantsSharedCollection,
      advice.merchant,
    );
    this.acquirersSharedCollection = this.acquirerService.addAcquirerToCollectionIfMissing<IAcquirer>(
      this.acquirersSharedCollection,
      advice.acquirer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.merchantService
      .query()
      .pipe(map((res: HttpResponse<IMerchant[]>) => res.body ?? []))
      .pipe(
        map((merchants: IMerchant[]) => this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(merchants, this.advice?.merchant)),
      )
      .subscribe((merchants: IMerchant[]) => (this.merchantsSharedCollection = merchants));

    this.acquirerService
      .query()
      .pipe(map((res: HttpResponse<IAcquirer[]>) => res.body ?? []))
      .pipe(
        map((acquirers: IAcquirer[]) => this.acquirerService.addAcquirerToCollectionIfMissing<IAcquirer>(acquirers, this.advice?.acquirer)),
      )
      .subscribe((acquirers: IAcquirer[]) => (this.acquirersSharedCollection = acquirers));
  }
}
