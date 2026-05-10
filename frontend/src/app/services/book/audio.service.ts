import { Injectable } from "@angular/core";

@Injectable({
	providedIn: 'root'
})
export class AudioService {
	openSound = new Audio('/livro/sounds/openCover.mp3');
	closeSound = new Audio('/livro/sounds/closeCover.mp3');
	pageFlipSound = new Audio('/livro/sounds/flipPage.wav');
	pagesFlipSound = new Audio('/livro/sounds/flipPages.wav');

	constructor() {
		this.openSound.preload = 'auto';
		this.closeSound.preload = 'auto';
		this.pageFlipSound.preload = 'auto';
		this.pagesFlipSound.preload = 'auto';
	}

	playFlips() {
		this.pagesFlipSound.currentTime = 0;
		this.pagesFlipSound.loop = true;
		this.pagesFlipSound.play();
  	}

	stopFlips() {
		this.pagesFlipSound.loop = false;
		this.pagesFlipSound.pause();
		this.pagesFlipSound.currentTime = 0;
	}

	playFlip() {
		this.pageFlipSound.currentTime = 0;
		this.pageFlipSound.play();
  	}

	playOpen() {
		this.openSound.currentTime = 0;
		this.openSound.play();
	}

	playClose() {
		this.closeSound.currentTime = 0;
		this.closeSound.play();
	}
}
