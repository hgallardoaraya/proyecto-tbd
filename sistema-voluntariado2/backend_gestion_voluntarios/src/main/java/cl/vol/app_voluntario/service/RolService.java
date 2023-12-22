package cl.vol.app_voluntario.service;

import cl.vol.app_voluntario.errors.ApiErrorException;
import cl.vol.app_voluntario.model.Institucion;
import cl.vol.app_voluntario.model.Rol;
import cl.vol.app_voluntario.repository.RolRepository;
import cl.vol.app_voluntario.request.CreateInstitucionRequest;
import cl.vol.app_voluntario.request.CreateRolRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {
    private final RolRepository rolRepository;

    public void createRol(CreateRolRequest request){
        try{
            Rol rol = new Rol();
            rol.setNombre(request.getNombre());
            System.out.println("creando rol");
            rolRepository.save(rol);
        }catch(Exception e){
            throw new ApiErrorException("No se pudo crear el rol " + e.getMessage());
        }
    }

    public List<Rol> getRoles(){
        return rolRepository.findAll();
    }

    public void updateRol(Integer id, Rol newRol){
        try{
            Rol rol = rolRepository.findById(id);
            if(rol == null) throw new ApiErrorException("El rol a actualizar no existe");
            if(newRol.getNombre() != null){
                rol.setNombre(newRol.getNombre());
            }
            rolRepository.set(rol);
        }catch(Exception e){
            throw new ApiErrorException("Error al actualizar el rol " + e.getMessage());
        }
    }

    public void deleteRol(Integer idRol){
        try{
            rolRepository.delete(idRol);
        }catch(Exception e){
            throw new ApiErrorException("Error al borrar el rol " + e.getMessage());
        }
    }
}
