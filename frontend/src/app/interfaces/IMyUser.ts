import { IResponse } from "./IResponse"

export interface IMyUser extends IResponse {
	data: User
}

export interface IProgresso extends IResponse {
	data: {
		chapter: number
		subtopic: number
	}
}

interface User {
	id: number
    name: string
	nickname: string
	email: string
	telefone: string
	role: string
}
