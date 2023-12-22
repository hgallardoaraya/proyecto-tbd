package cl.vol.app_voluntario.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateEmeHabilidadRequest {
    @NotNull
    Integer idHabilidad;
}
