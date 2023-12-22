package cl.vol.app_voluntario.request;

import cl.vol.app_voluntario.model.Coordinador;
import cl.vol.app_voluntario.model.Rol;
import cl.vol.app_voluntario.model.Voluntario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUsuarioRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private Integer idInstitucion;
    private boolean voluntario;
    private boolean coordinador;
    private Double longit;
    private Double latit;
}
