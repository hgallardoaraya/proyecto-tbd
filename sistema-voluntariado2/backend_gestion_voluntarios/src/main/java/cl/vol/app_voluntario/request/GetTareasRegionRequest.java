package cl.vol.app_voluntario.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetTareasRegionRequest {
    @NotEmpty( message = "Debes ingresar una región.")
    String nombreRegion;
}
