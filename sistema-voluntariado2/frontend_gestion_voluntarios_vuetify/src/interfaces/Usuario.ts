import Rol from './Rol'
import Coordinador from './Coordinador';
import Voluntario from './Voluntario';

export default interface Usuario {
    id?: number;
    nombre?: string;
    apellido?: string;
    email?: string;
    password?: string;
    roles?: Rol[];
    coordinador?: Coordinador;
    voluntario?: Voluntario; 
    latit?: number,
    longit?: number
}   