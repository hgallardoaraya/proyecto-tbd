package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Coordinador;

import java.util.List;

public interface CoordinadorRepository {
    Coordinador save(Coordinador coordinador);
    Coordinador findByUserId(Integer idUsuario);
    Coordinador findById(Integer idCoordinador);

    void delete(Integer idCoordinador);
    void set(Coordinador coordinador);
}
