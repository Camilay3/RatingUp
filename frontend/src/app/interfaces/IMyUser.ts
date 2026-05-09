import { IResponse } from "./IResponse"

export interface IMyUser extends IResponse {
	data: User
}

interface User {
	id: number
    name: string
	nickname: string
	email: string
	telefone: string
	role: string
}
