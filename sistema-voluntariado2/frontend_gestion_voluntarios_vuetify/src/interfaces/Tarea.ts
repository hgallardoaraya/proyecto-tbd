import Emergencia from "./Emergencia";
import Estado from "./Estado";
import Habilidad from "./Habilidad";

export default interface Tarea {
    id?: number;
    nombre?: string;
    descripcion?: string;
    voluntariosRequeridos?: number;
    voluntariosInscritos?: number;
    fechaInicio?: string;
    fechaFin?: string;
    estado?: Estado;
    habilidades?: Habilidad[];
    emergencia?: Emergencia;
    latit?: number,
    longit?: number
}