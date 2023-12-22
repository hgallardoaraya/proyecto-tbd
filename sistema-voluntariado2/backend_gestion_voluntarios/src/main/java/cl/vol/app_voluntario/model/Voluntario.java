package cl.vol.app_voluntario.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Voluntario {
    private Integer id;
    private Usuario usuario;
    private Integer puntaje;
    private List<Tarea> tareas;
    private List<Habilidad> habilidades;
}
