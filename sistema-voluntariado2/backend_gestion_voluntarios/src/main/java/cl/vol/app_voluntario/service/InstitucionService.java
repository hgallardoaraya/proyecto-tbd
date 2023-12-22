package cl.vol.app_voluntario.service;

import cl.vol.app_voluntario.errors.ApiErrorException;
import cl.vol.app_voluntario.errors.InvalidDatesException;
import cl.vol.app_voluntario.model.Emergencia;
import cl.vol.app_voluntario.model.Habilidad;
import cl.vol.app_voluntario.model.Institucion;
import cl.vol.app_voluntario.repository.InstitucionRepository;
import cl.vol.app_voluntario.request.CreateEmergenciaRequest;
import cl.vol.app_voluntario.request.CreateInstitucionRequest;
import cl.vol.app_voluntario.request.UpdateHabilidadRequest;
import cl.vol.app_voluntario.request.UpdateInstitucionRequest;
import cl.vol.app_voluntario.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitucionService {
    private final InstitucionRepository institucionRepository;
    public void createInstitucion(CreateInstitucionRequest request){
        try{
            Institucion institucion = new Institucion();
            institucion.setNombre(request.getNombre());
            institucion.setDescripcion(request.getDescripcion());
            institucionRepository.save(institucion);
        }catch(Exception e){
            throw new ApiErrorException("No se pudo crear la institución " + e.getMessage());
        }
    }

    public List<Institucion> getInstituciones(){
        return institucionRepository.findAll();
    }

    public void updateInstitucion(Integer id, Institucion newInstitucion){
        try{
            Institucion institucion = institucionRepository.findById(id);
            if(institucion == null) throw new ApiErrorException("La institución a actualizar no existe");
            if(newInstitucion.getNombre() != null){
                institucion.setNombre(newInstitucion.getNombre());
            }
            if(newInstitucion.getDescripcion() != null) {
                institucion.setDescripcion(newInstitucion.getDescripcion());
            }
            institucionRepository.set(institucion);
        }catch(Exception e){
            throw new ApiErrorException("Error al actualizar la institución " + e.getMessage());
        }
    }

    public void deleteInstitucion(Integer idInstitucion) {
        try{
            Institucion institucion = institucionRepository.findById(idInstitucion);
            if(institucion == null) throw new ApiErrorException("La institucion no existe.");
            institucionRepository.delete(idInstitucion);
        }catch(Exception e){
            throw new ApiErrorException("Error al eliminar la institución " + e.getMessage());
        }
    }
}
