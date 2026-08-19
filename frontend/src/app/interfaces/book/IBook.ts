import { IResponse } from "../IResponse";

export type IPage = ICapitulo | ISubtopico;
export type PageData = IPage | IHome;

export interface IBook extends IResponse {
	data: {
		pages: ISheet[]
		totalPages: number
	}
}

export interface ISheet {
	front: IPage
	verse: IPage
}

interface IBasePage {
	type: 'capitulo' | 'subtópico'
	displayOrder: number
	chapterId: number | null
	title: string
}

interface IHome {
	type: 'home'
	isFirstHome: boolean
	chunkOffset: number
	nickname: string | null
	summary: ISheet[]
}

interface ICapitulo extends IBasePage {
	type: 'capitulo'
	chapterId: null
}

export interface ISubtopico extends IBasePage {
	id: number
	type: 'subtópico'
	chapterId: number
	isBlocked: boolean
	subtopicImageUrl: string
	lockOpen?: boolean
}

export interface ISubtopicoContent extends ISubtopico, IResponse {
	content: string
	practiceExplanation: string | null;
}

export type HomePage = {
	type: 'home';
	nickname: string | null;
	isFirstHome: boolean;
	summary: ISheet[];
	chunkOffset: number;
};

export type BookItem = {
	front?: PageData;
	verse?: PageData | null;
	capa?: string;
	frenteCapa?: boolean;
};
