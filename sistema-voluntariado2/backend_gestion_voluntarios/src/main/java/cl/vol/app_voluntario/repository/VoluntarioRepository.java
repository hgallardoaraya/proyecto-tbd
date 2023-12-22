package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Voluntario;
import cl.vol.app_voluntario.request.CreateVolTareaRequest;

public interface VoluntarioRepository {
    Voluntario save(Voluntario voluntario);
    Voluntario findVoluntarioByUserId(Integer idUsuario);

    Voluntario findById(Integer idUsuario);
    void saveVolHabilidad(Integer idVoluntario, Integer idHabilidad);
    void deleteVolHabilidad(Integer idVoluntario, Integer idHabilidad);
    void setVolHabilidad(Integer id, Integer idHabilidad, Integer newIdHabilidad);
    void saveVolTarea(Integer idVoluntario, Integer idTarea, CreateVolTareaRequest request);
    void deleteVolTarea(Integer idVoluntario, Integer idTarea);
    void deleteVolHabilidadByVoluntarioId(Integer idVoluntario);
    void delete(Integer idVoluntario);
    void setVolTarea(Integer id, Integer idTarea, Integer newIdTarea);

}
