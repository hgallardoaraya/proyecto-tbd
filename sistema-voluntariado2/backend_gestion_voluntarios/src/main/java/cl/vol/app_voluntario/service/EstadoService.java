package cl.vol.app_voluntario.service;

import cl.vol.app_voluntario.errors.ApiErrorException;
import cl.vol.app_voluntario.errors.InvalidDatesException;
import cl.vol.app_voluntario.model.Emergencia;
import cl.vol.app_voluntario.model.Estado;
import cl.vol.app_voluntario.model.Institucion;
import cl.vol.app_voluntario.repository.EstadoRepository;
import cl.vol.app_voluntario.request.CreateEmergenciaRequest;
import cl.vol.app_voluntario.request.CreateEstadoRequest;
import cl.vol.app_voluntario.request.UpdateEmergenciaRequest;
import cl.vol.app_voluntario.request.UpdateEstadoRequest;
import cl.vol.app_voluntario.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoService {
    private final EstadoRepository estadoRepository;

    public List<Estado> getEstados(){
        try{
            return estadoRepository.findAll();
        }catch(Exception e){
            throw new ApiErrorException("Los estados buscados no existen.");
        }
    }

    public void updateEstado(Integer id, UpdateEstadoRequest newEstado){
        try{
            Estado estado = estadoRepository.findById(id);
            if(estado == null) throw new ApiErrorException("El estado a actualizar no existe");
            if(newEstado.getDescripcion() != null) {
                estado.setDescripcion(newEstado.getDescripcion());
            }
            estadoRepository.set(estado);
        }catch(Exception e){
            throw new ApiErrorException("Error al actualizar la emergencia." + e.getMessage());
        }
    }

    public void createEstado(CreateEstadoRequest request){
        try{
            Estado estado = new Estado();
            estado.setDescripcion(request.getDescripcion());
            estadoRepository.save(estado);
        }catch(Exception e){
            throw new ApiErrorException("El estado no ha podido ser creado." + e.getMessage());
        }
    }

    public void deleteEstado(Integer idEstado) {
        try{
            if(estadoRepository.findById(idEstado) == null) throw new ApiErrorException("El estado no existe");
            estadoRepository.delete(idEstado);
        }catch (Exception e){
            throw new ApiErrorException("El estado no ha podido ser eliminado. " + e.getMessage());
        }
    }
}
