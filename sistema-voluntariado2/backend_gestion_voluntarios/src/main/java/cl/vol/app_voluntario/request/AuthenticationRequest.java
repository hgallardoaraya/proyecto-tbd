package cl.vol.app_voluntario.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Debe ingresar un email.")
    @Email(message = "El email ingresado no es válido.")
    private String email;
    @NotBlank(message = "Debe ingresar una contraseña.")
    private String password;
    @NotNull(message = "Debe ingresar el tipo de usuario.")
    private Integer idRol;
}
