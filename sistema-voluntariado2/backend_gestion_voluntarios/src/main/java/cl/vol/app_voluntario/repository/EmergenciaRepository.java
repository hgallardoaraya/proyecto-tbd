package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Emergencia;

import java.util.List;

public interface EmergenciaRepository {
    Emergencia save(Emergencia emergencia);
    Emergencia findById(Integer idEmergencia);
    List<Emergencia> findAll();
    Emergencia findByTareaId(Integer idTarea);
    Integer findEmeHabilidadIdByHabilidadId(Integer idHabilidad);
    List<Emergencia> findAllByHabilidadId(Integer idHabilidad);
    void set(Emergencia emergencia);

    void delete(Integer idEmergencia);

    void saveEmeHabilidad(Integer idEmergencia, Integer idHabilidad);
    void deleteEmeHabilidad(Integer idEmergencia, Integer idHabilidad);
    void setEmeHabilidad(Integer idEmergencia, Integer idHabilidad, Integer newIdHabilidad);
}
