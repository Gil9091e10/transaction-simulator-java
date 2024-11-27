import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AcquirerService } from '../service/acquirer.service';
import { IAcquirer } from '../acquirer.model';
import { AcquirerFormService } from './acquirer-form.service';

import { AcquirerUpdateComponent } from './acquirer-update.component';

describe('Acquirer Management Update Component', () => {
  let comp: AcquirerUpdateComponent;
  let fixture: ComponentFixture<AcquirerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let acquirerFormService: AcquirerFormService;
  let acquirerService: AcquirerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AcquirerUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AcquirerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AcquirerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    acquirerFormService = TestBed.inject(AcquirerFormService);
    acquirerService = TestBed.inject(AcquirerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const acquirer: IAcquirer = { id: 456 };

      activatedRoute.data = of({ acquirer });
      comp.ngOnInit();

      expect(comp.acquirer).toEqual(acquirer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAcquirer>>();
      const acquirer = { id: 123 };
      jest.spyOn(acquirerFormService, 'getAcquirer').mockReturnValue(acquirer);
      jest.spyOn(acquirerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acquirer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acquirer }));
      saveSubject.complete();

      // THEN
      expect(acquirerFormService.getAcquirer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(acquirerService.update).toHaveBeenCalledWith(expect.objectContaining(acquirer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAcquirer>>();
      const acquirer = { id: 123 };
      jest.spyOn(acquirerFormService, 'getAcquirer').mockReturnValue({ id: null });
      jest.spyOn(acquirerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acquirer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acquirer }));
      saveSubject.complete();

      // THEN
      expect(acquirerFormService.getAcquirer).toHaveBeenCalled();
      expect(acquirerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAcquirer>>();
      const acquirer = { id: 123 };
      jest.spyOn(acquirerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acquirer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(acquirerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
