import Tarea from "./Tarea";

export default interface Estado {
    id?: number;
    descripcion?: string;
    tarea?: Tarea;
}