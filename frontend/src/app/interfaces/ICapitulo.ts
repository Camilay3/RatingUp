export interface ICapitulo {
    tipo: 'capitulo';
    id: number;
    titulo: string;
    subtopicos: ISubtopico[];
}

export interface ISubtopico {
    tipo: 'subtopico';
    id: number;
    idCapitulo: number;
    titulo: string;
    url: string;
}
