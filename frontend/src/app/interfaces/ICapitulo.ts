export interface ICapitulo {
    tipo: 'capitulo';
    id: number;
    titulo: string;
}

export interface ISubtopico {
    tipo: 'subtopico';
    id: number;
    idCapitulo: number;
    titulo: string;
	isBlocked?: boolean;
}

export interface IHome {
	tipo: 'home';
	nickname: string;
}
