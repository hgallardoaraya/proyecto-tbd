import Coordinador from "./Coordinador";
import Emergencia from "./Emergencia";

export default interface Institucion {
    id?: number;
    nombre?: string;
    descripcion?: string;
    coordinadores?: Coordinador[];
    emergencias?: Emergencia[];
}