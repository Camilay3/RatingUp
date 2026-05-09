export interface IMyUser {
	status: true | false
	message: string
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
