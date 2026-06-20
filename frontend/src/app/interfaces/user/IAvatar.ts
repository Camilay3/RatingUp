import { IResponse } from "../IResponse"

export interface IAvatarItem {
	imageName: string
	avatarurl: string
}

export interface IAvatarList extends IResponse {
	data: IAvatarItem[]
}