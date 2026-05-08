export type IPage = ICapitulo | ISubtopico;
export type PageData = IPage | IHome;

export interface IBook {
	status: true | false
	message: string
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
	id: number
	idChapter: number | null
	title: string
}

interface IHome extends IBasePage {
	type: 'home'
	nickname: string
	summary: ISheet[]
}

interface ICapitulo extends IBasePage {
	type: 'capitulo'
	idChapter: null
}

interface ISubtopico extends IBasePage {
	type: 'subtópico'
	idChapter: number
	isBlocked: boolean
}
