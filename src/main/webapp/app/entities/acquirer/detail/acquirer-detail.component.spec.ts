import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AcquirerDetailComponent } from './acquirer-detail.component';

describe('Acquirer Management Detail Component', () => {
  let comp: AcquirerDetailComponent;
  let fixture: ComponentFixture<AcquirerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AcquirerDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./acquirer-detail.component').then(m => m.AcquirerDetailComponent),
              resolve: { acquirer: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AcquirerDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AcquirerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load acquirer on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AcquirerDetailComponent);

      // THEN
      expect(instance.acquirer()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
