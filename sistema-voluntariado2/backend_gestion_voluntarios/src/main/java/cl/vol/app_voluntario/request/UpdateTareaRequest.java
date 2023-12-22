package cl.vol.app_voluntario.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;

@Data
public class UpdateTareaRequest {
    @Length(min = 5, max=20, message = "El nombre debe tener entre 5 y 30 caracteres.")
    private String nombre;
    @Length(max=100, message = "La descripción debe tener máximo 100 caracteres.")
    private String descripcion;
    @Min(value = 1, message = "Debe haber mínimo 1 voluntario requerido.")
    private Integer voluntariosRequeridos;
    private Integer voluntariosInscritos;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaInicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaFin;
    private Integer idEmergencia;
    private Integer idEstado;
    private Integer[] idsHabilidades;
    private Double longit;
    private Double latit;
}
