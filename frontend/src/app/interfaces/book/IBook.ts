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
	front: PageData
	verse: IPage
}

interface IBasePage {
	type: 'capitulo' | 'subtópico' | 'home'
	displayOrder: number
	chapterId: number | null
	title: string
}

interface IHome extends IBasePage {
	type: 'home'
	isFirstHome: boolean
	chunkOffset: number
	nickname: string
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
