package cl.vol.app_voluntario.request;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateEmergenciaRequest {
    @NotBlank(message = "El nombre no puede estar vacio.")
    private String nombre;
    @NotBlank(message = "La descripción no puede estar vacia.")
    private String descripcion;
    @NotNull(message = "Debe ingresar una fecha válida.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaInicio;
    @NotNull(message = "Debe ingresar una fecha válida.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaFin;
    @NotNull(message = "Debe ingresar una institución válida.")
    private Integer id_institucion;
    @NotNull(message = "Debe ingresar una longitud válida.")
    private Double longit;
    @NotNull(message = "Debe ingresar una latitud válida.")
    private Double latit;
}
