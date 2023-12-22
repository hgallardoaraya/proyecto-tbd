package cl.vol.app_voluntario.service;

import cl.vol.app_voluntario.errors.ApiErrorException;
import cl.vol.app_voluntario.model.Estado;
import cl.vol.app_voluntario.model.Habilidad;
import cl.vol.app_voluntario.repository.HabilidadRepository;
import cl.vol.app_voluntario.request.UpdateEstadoRequest;
import cl.vol.app_voluntario.request.UpdateHabilidadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabilidadService {
    private final HabilidadRepository habilidadRepository;

    public Habilidad createHabilidad(String descripcion){
        Habilidad habilidad = new Habilidad();
        habilidad.setDescripcion(descripcion);
        return habilidadRepository.save(habilidad);
    }

    public void updateHabilidad(Integer id, UpdateHabilidadRequest newHabilidad){
        try{
            Habilidad habilidad = habilidadRepository.findById(id);
            if(habilidad == null) throw new ApiErrorException("La habilidad a actualizar no existe");
            if(newHabilidad.getDescripcion() != null) {
                habilidad.setDescripcion(newHabilidad.getDescripcion());
            }
            habilidadRepository.set(habilidad);
        }catch(Exception e){
            throw new ApiErrorException("Error al actualizar la habilidad." + e.getMessage());
        }
    }

    public List<Habilidad> getHabilidades() {
        try{
            return habilidadRepository.findAll();
        }catch (Exception e){
            throw new ApiErrorException("Hubo un error al obtener las habilidades. " + e.getMessage());
        }
    }

    public void deleteHabilidad(Integer idHabilidad) {
        try{
            if(habilidadRepository.findById(idHabilidad) == null) throw new ApiErrorException("La habilidad no existe.");
            habilidadRepository.delete(idHabilidad);
        }catch(Exception e){
            throw new ApiErrorException("Hubo un error al eliminar la habilidad. " + e.getMessage());
        }
    }
}
