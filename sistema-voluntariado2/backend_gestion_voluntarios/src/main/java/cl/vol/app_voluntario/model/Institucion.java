package cl.vol.app_voluntario.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Institucion {
    private Integer id;

    private String nombre;

    private String descripcion;

    private List<Coordinador> coordinadores = new ArrayList<>();

    private List<Emergencia> emergencias = new ArrayList<>();

}
