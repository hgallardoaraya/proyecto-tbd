package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Habilidad;
import cl.vol.app_voluntario.model.Tarea;

import java.util.List;

public interface TareaRepository {
    Tarea save(Tarea tarea);

    boolean saveTareaHabilidades(Integer idTarea, List<Habilidad> habilidades);

    Tarea findById(Integer idTarea);

    List<Tarea> findAllByVoluntarioId(Integer idVoluntario);

    List<Tarea> findAll();

    void set(Tarea tarea);

    void delete(Integer idTarea);
    void deleteTareaHabilidad(Integer idTarea);

    List<Tarea> findAllByNombreRegion(String nombreRegion);

    void deleteRankingByVoluntarioId(Integer idUsuario);
    void saveTareaHabilidad(Integer idTarea, Integer idHabilidad);
    void deleteTareaHabilidad(Integer idTarea, Integer idHabilidad);
    void setTareaHabilidad(Integer idTarea, Integer idHabilidad, Integer newIdHabilidad);
    void deleteAllTareaHabilidad(Integer idTarea);
}
