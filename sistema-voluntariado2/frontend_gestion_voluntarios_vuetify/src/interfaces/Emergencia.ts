import Habilidad from "./Habilidad";
import Tarea from "./Tarea";

export default interface Emergencia {
    id?: number;
    nombre?: string;
    descripcion?: string;
    fechaInicio?: Date;
    fechaFin?: Date;
    habilidades?: Habilidad[];
    tareas?: Tarea;
}