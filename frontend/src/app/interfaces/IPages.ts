import { ICapitulo, ISubtopico, IHome } from "./ICapitulo";

export type IConteudoPage = ICapitulo | ISubtopico | IHome | null;
export interface IPages {
    frente: IConteudoPage;
    verso: IConteudoPage;
}
