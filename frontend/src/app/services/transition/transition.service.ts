import { Injectable, signal } from '@angular/core';

export interface TransitionState {
    active: boolean;
    imageUrl: string;
    dominantColor: string;
    phase: 'idle' | 'extracting' | 'zoom' | 'fade' | 'done';
    rect: DOMRect | null;
}

@Injectable({ providedIn: 'root' })
export class TransitionService {

    readonly state = signal<TransitionState>({
        active: false,
        imageUrl: '',
        dominantColor: '#5A3E2B',
        phase: 'idle',
        rect: null,
    });

    readonly PRIMARY_COLOR = '#5A3E2B';

    async extractDominantColor(imgEl: HTMLImageElement): Promise<string> {
        return new Promise((resolve) => {
            const fallback = () => resolve(this.PRIMARY_COLOR);

            if (!imgEl.complete || imgEl.naturalWidth === 0) {
                fallback();
                return;
            }

            try {
                const SIZE = 16;
                const canvas = document.createElement('canvas');
                canvas.width = SIZE;
                canvas.height = SIZE;
                const ctx = canvas.getContext('2d', { willReadFrequently: true });
                if (!ctx) { fallback(); return; }

                ctx.drawImage(imgEl, 0, 0, SIZE, SIZE);
                const { data } = ctx.getImageData(0, 0, SIZE, SIZE);

                let rSum = 0, gSum = 0, bSum = 0, weight = 0;
                for (let i = 0; i < data.length; i += 4) {
                    const r = data[i], g = data[i + 1], b = data[i + 2], a = data[i + 3];
                    if (a < 128) continue; // skip transparent

                    const max = Math.max(r, g, b);
                    const min = Math.min(r, g, b);
                    const sat = max === 0 ? 0 : (max - min) / max;
                    const w = 0.2 + sat * 0.8;

                    rSum += r * w;
                    gSum += g * w;
                    bSum += b * w;
                    weight += w;
                }

                if (weight === 0) { fallback(); return; }

                const toHex = (v: number) =>
                Math.round(Math.min(255, Math.max(0, v / weight)))
                .toString(16).padStart(2, '0');

                resolve(`#${toHex(rSum)}${toHex(gSum)}${toHex(bSum)}`);
                } catch {
                    fallback();
                }
        });
    }

    async startTransition(imgEl: HTMLImageElement, rect: DOMRect): Promise<void> {
        const dominantColor = await this.extractDominantColor(imgEl);

        this.state.set({
            active: true,
            imageUrl: imgEl.currentSrc || imgEl.src,
            dominantColor,
            phase: 'zoom',
            rect,
        });
    }

    finishTransition(): void {
        this.state.set({
        active: false,
        imageUrl: '',
        dominantColor: this.PRIMARY_COLOR,
        phase: 'done',
        rect: null,
        });
    }

    reset(): void {
        this.state.set({
            active: false,
            imageUrl: '',
            dominantColor: this.PRIMARY_COLOR,
            phase: 'idle',
            rect: null,
        });
    }
}