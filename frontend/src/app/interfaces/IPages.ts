import { ICapitulo, ISubtopico } from "./ICapitulo";

export type IConteudoPage = ICapitulo | ISubtopico;
export interface IPages {
    capa?: string;
    frenteCapa?: boolean;
    frente?: IConteudoPage;
    verso?: IConteudoPage;
}
