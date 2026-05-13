export interface IUser {
  email: string;
  senha: string;
  nome: string;
  nickname: string;
  telefone: string;
  role : string;
}

export interface ILoginPayload {
  email: string;
  password: string;
}