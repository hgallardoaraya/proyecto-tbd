package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Habilidad;

import java.util.List;

public interface HabilidadRepository {
    Habilidad save(Habilidad habilidad);

    Habilidad findById(Integer idHabilidad);

    List<Habilidad> findAllByVoluntarioId(Integer idVoluntario);

    List<Habilidad> findAllByEmergenciaId(Integer idEmergencia);

    List<Habilidad> findAllByTareaId(Integer idTarea);
    void set(Habilidad habilidad);

    List<Habilidad> findAll();
    void delete(Integer idHabilidad);
}
