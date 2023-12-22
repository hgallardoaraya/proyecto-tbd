package cl.vol.app_voluntario.request;

import lombok.Data;

@Data
public class CreateVolTareaRequest {
    Integer puntaje;
    Integer flag_participa;
    Integer flag_invitado;
}
