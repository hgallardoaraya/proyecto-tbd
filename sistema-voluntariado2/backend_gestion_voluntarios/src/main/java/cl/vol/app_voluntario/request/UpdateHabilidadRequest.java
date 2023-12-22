package cl.vol.app_voluntario.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateHabilidadRequest {
    private String descripcion;
}