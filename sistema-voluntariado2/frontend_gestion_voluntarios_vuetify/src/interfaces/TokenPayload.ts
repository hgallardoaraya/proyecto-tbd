import Rol from "./Rol";

export default interface TokenPayload {
    exp?: number,
    iat?: number,
    idUsuario?: number,
    rol?: Rol,
    sub?: string
}