package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Rol;

import java.util.List;

public interface RolRepository {
    void save(Rol rol);
    List<Rol> findAll();
    Rol findById(int idRol);
    List<Rol> findAllByUserId(int idUsuario);
    void set(Rol rol);
    void delete(Integer idRol);
}
