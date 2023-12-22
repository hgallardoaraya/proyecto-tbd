package cl.vol.app_voluntario.request;

import cl.vol.app_voluntario.model.Emergencia;
import cl.vol.app_voluntario.model.Estado;
import cl.vol.app_voluntario.model.Habilidad;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateTareaRequest {
    @NotNull
    @Length(min = 5, max=20, message = "El nombre debe tener entre 5 y 30 caracteres.")
    private String nombre;
    @NotNull
    @Length(max=300, message = "La descripción debe tener máximo 300 caracteres.")
    private String descripcion;
    @Min(value = 1, message = "Debe haber mínimo 1 voluntario requerido.")
    private Integer voluntariosRequeridos;
    private Integer voluntariosInscritos;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaInicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaFin;
    @NotNull(message = "Debe ingresar una emergencia.")
    private Integer idEmergencia;
    private Integer idEstado;
    @NotNull(message = "Debe seleccionar habilidades.")
    private Integer[] idsHabilidades;
    @NotNull(message = "Debe seleccionar una ubicación.")
    private Double longit;
    @NotNull(message = "Debe seleccionar una ubicación.")
    private Double latit;
}
