import Tarea from "./Tarea";
import Habilidad from "./Habilidad";

export default interface Voluntario {
    id?: number;
    tareas?: Tarea[];
    habilidades?: Habilidad[];
}