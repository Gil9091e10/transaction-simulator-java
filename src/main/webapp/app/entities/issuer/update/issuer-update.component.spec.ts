import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IssuerService } from '../service/issuer.service';
import { IIssuer } from '../issuer.model';
import { IssuerFormService } from './issuer-form.service';

import { IssuerUpdateComponent } from './issuer-update.component';

describe('Issuer Management Update Component', () => {
  let comp: IssuerUpdateComponent;
  let fixture: ComponentFixture<IssuerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let issuerFormService: IssuerFormService;
  let issuerService: IssuerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [IssuerUpdateComponent],
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
      .overrideTemplate(IssuerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IssuerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    issuerFormService = TestBed.inject(IssuerFormService);
    issuerService = TestBed.inject(IssuerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const issuer: IIssuer = { id: 456 };

      activatedRoute.data = of({ issuer });
      comp.ngOnInit();

      expect(comp.issuer).toEqual(issuer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssuer>>();
      const issuer = { id: 123 };
      jest.spyOn(issuerFormService, 'getIssuer').mockReturnValue(issuer);
      jest.spyOn(issuerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issuer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: issuer }));
      saveSubject.complete();

      // THEN
      expect(issuerFormService.getIssuer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(issuerService.update).toHaveBeenCalledWith(expect.objectContaining(issuer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssuer>>();
      const issuer = { id: 123 };
      jest.spyOn(issuerFormService, 'getIssuer').mockReturnValue({ id: null });
      jest.spyOn(issuerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issuer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: issuer }));
      saveSubject.complete();

      // THEN
      expect(issuerFormService.getIssuer).toHaveBeenCalled();
      expect(issuerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIssuer>>();
      const issuer = { id: 123 };
      jest.spyOn(issuerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ issuer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(issuerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
