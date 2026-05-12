import { IResponse } from "../IResponse"

export interface IMyUser extends IResponse {
	data: {
		id: number
		name: string
		nickname: string
		email: string
		telefone: string
		role: string
	}
}

export interface IProgresso extends IResponse {
	data: {
		chapter: number
		subtopic: number
	}
}
