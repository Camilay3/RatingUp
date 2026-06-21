import {
  Component,
  OnInit,
  OnDestroy,
  inject,
  signal,
  effect,
  NgZone,
} from '@angular/core';
import { Router } from '@angular/router';
import { TransitionService } from '../../services/transition/transition.service';

const ANIMATION_DURATION_MS = 520;

@Component({
  selector: 'app-transition-overlay',
  standalone: true,
  imports: [],
  template: `
    <div
      class="overlay"
      [class.active]="isActive()"
      [style.--dominant-color]="dominantColor()"
      [style.--primary-color]="'#5A3E2B'"
      [style.--img-left]="imgLeft() + 'px'"
      [style.--img-top]="imgTop() + 'px'"
      [style.--img-width]="imgWidth() + 'px'"
      [style.--img-height]="imgHeight() + 'px'"
    >
      <div class="bg-layer" [class.animating]="isAnimating()"></div>

      <img
        class="img-clone"
        [class.animating]="isAnimating()"
        [src]="imageUrl()"
        alt=""
        aria-hidden="true"
        [style.top.px]="imgTop()"
        [style.left.px]="imgLeft()"
        [style.width.px]="imgWidth()"
        [style.height.px]="imgHeight()"
      />
    </div>
  `,
  styleUrls: ['./transition-overlay.component.scss'],
})
export class TransitionOverlayComponent implements OnInit, OnDestroy {
  private readonly transitionService = inject(TransitionService);
  private readonly router = inject(Router);
  private readonly ngZone = inject(NgZone);

  isActive  = signal(false);
  isAnimating = signal(false);
  imageUrl  = signal('');
  dominantColor = signal('#5A3E2B');
  imgTop    = signal(0);
  imgLeft   = signal(0);
  imgWidth  = signal(0);
  imgHeight = signal(0);

  private pendingSubtopicoId: number | null = null;
  private navigationTimer: ReturnType<typeof setTimeout> | null = null;
  private prepareListener!: EventListener;

  constructor() {
    effect(() => {
      const state = this.transitionService.state();
      if (state.phase === 'zoom' && state.rect) {
        this.ngZone.run(() => this.beginAnimation(state));
      }
      if (state.phase === 'idle' || state.phase === 'done') {
        this.ngZone.run(() => this.deactivate());
      }
    });
  }

  ngOnInit(): void {
    this.prepareListener = (e: Event) => {
      const detail = (e as CustomEvent).detail;
      if (detail?.subtopicoId !== undefined) {
        this.pendingSubtopicoId = detail.subtopicoId;
      }
    };
    document.addEventListener('transition:prepare', this.prepareListener);
  }

  ngOnDestroy(): void {
    document.removeEventListener('transition:prepare', this.prepareListener);
    if (this.navigationTimer) clearTimeout(this.navigationTimer);
  }

  private beginAnimation(state: ReturnType<typeof this.transitionService.state>): void {
    const rect = state.rect!;

    this.imageUrl.set(state.imageUrl);
    this.dominantColor.set(state.dominantColor);
    this.imgTop.set(rect.top);
    this.imgLeft.set(rect.left);
    this.imgWidth.set(rect.width);
    this.imgHeight.set(rect.height);
    this.isActive.set(true);

    requestAnimationFrame(() => {
      requestAnimationFrame(() => {
        this.isAnimating.set(true);

        this.navigationTimer = setTimeout(() => {
          this.navigateNow();
        }, ANIMATION_DURATION_MS);
      });
    });
  }

  private navigateNow(): void {
    if (this.pendingSubtopicoId === null) {
      this.transitionService.finishTransition();
      return;
    }

    const id = this.pendingSubtopicoId;
    this.pendingSubtopicoId = null;

    this.router.navigate(['/subtopico'], {
      state: { subtopicoId: id },
    }).then(() => {
      setTimeout(() => this.transitionService.finishTransition(), 80);
    }).catch(() => {
      this.transitionService.finishTransition();
    });
  }

  private deactivate(): void {
    this.isActive.set(false);
    this.isAnimating.set(false);
  }
}