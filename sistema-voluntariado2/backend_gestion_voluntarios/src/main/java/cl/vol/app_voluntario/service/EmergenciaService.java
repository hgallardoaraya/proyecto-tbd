package cl.vol.app_voluntario.service;

import cl.vol.app_voluntario.errors.ApiErrorException;
import cl.vol.app_voluntario.errors.InstitucionNotFoundException;
import cl.vol.app_voluntario.errors.InvalidDatesException;
import cl.vol.app_voluntario.model.*;
import cl.vol.app_voluntario.repository.EmergenciaRepository;
import cl.vol.app_voluntario.repository.HabilidadRepository;
import cl.vol.app_voluntario.repository.InstitucionRepository;
import cl.vol.app_voluntario.repository.UsuarioRepository;
import cl.vol.app_voluntario.request.CreateEmergenciaRequest;
import cl.vol.app_voluntario.request.CreateTareaRequest;
import cl.vol.app_voluntario.request.UpdateEmergenciaRequest;
import cl.vol.app_voluntario.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergenciaService {
    private final EmergenciaRepository emergenciaRepository;
    private final InstitucionRepository institucionRepository;
    private final HabilidadRepository habilidadRepository;
    private final UsuarioRepository usuarioRepository;
    public ResponseEntity<Emergencia> createEmergencia(CreateEmergenciaRequest request){
        try{
            if(!ValidationUtil.validateDates(request.getFechaInicio(), request.getFechaFin())){
                throw new InvalidDatesException("La fecha de inicio debe ser menor a la fecha final");
            };
            Institucion institucion = institucionRepository.findById(request.getId_institucion());
            if(institucion == null) throw new ApiErrorException("La institución no existe.\n");
            Emergencia emergencia = new Emergencia();
            emergencia.setNombre(request.getNombre());
            emergencia.setDescripcion(request.getDescripcion());
            emergencia.setFechaInicio(request.getFechaInicio());
            emergencia.setFechaFin(request.getFechaFin());
            emergencia.setInstitucion(institucion);
            emergencia.setLongit(request.getLongit());
            emergencia.setLatit(request.getLatit());
            return new ResponseEntity<>(emergenciaRepository.save(emergencia), HttpStatus.CREATED);
        }catch(Exception e){
            throw new ApiErrorException("Error al crear la emergencia " + e.getMessage());
        }
    }

    public List<Emergencia> getEmergencias(){
        return emergenciaRepository.findAll();
    }

    public List<Habilidad> getHabilidadesEmergencia(Integer idEmergencia) {
        try{
            if(emergenciaRepository.findById(idEmergencia) == null) throw new ApiErrorException("La emergencia no existe.");
            return habilidadRepository.findAllByEmergenciaId(idEmergencia);
        }catch(Exception e){
            throw new ApiErrorException("Ha ocurrido un error al obtener las emergencias " + e.getMessage());
        }
    }

    public void updateEmergencia(Integer id, UpdateEmergenciaRequest newEmergencia){
        try{
            Emergencia emergencia = emergenciaRepository.findById(id);
            if(emergencia == null) throw new ApiErrorException("La emergencia a actualizar no existe");
            if(newEmergencia.getNombre() != null) {
                emergencia.setNombre(newEmergencia.getNombre());
            }
            if(newEmergencia.getDescripcion() != null){
                emergencia.setDescripcion(newEmergencia.getDescripcion());
            }
            if(newEmergencia.getFechaInicio() != null){
                emergencia.setFechaInicio(newEmergencia.getFechaInicio());
            }
            if(newEmergencia.getFechaFin() != null){
                emergencia.setFechaFin(newEmergencia.getFechaFin());
            }
            if(newEmergencia.getId_institucion() != null){
                if(institucionRepository.findById(newEmergencia.getId_institucion()) == null) throw new ApiErrorException("La institución no existe.");
                emergencia.setInstitucion(institucionRepository.findById(newEmergencia.getId_institucion()));
            }
            if(newEmergencia.getLongit() != null){
                emergencia.setLongit(newEmergencia.getLongit());
            }
            if(newEmergencia.getLatit() != null){
                emergencia.setLatit(newEmergencia.getLatit());
            }
            emergenciaRepository.set(emergencia);
        }catch(Exception e){
            throw new ApiErrorException("Error al actualizar la emergencia." + e.getMessage());
        }
    }

    public List<Usuario> getVoluntariosEmergencia(Integer idEmergencia){
        try{
            if(emergenciaRepository.findById(idEmergencia) == null) throw new ApiErrorException("La emergencia no existe.");
            return usuarioRepository.findAllVoluntariosByEmergenciaId(idEmergencia);
        }catch(Exception e){
            throw new ApiErrorException("Ha ocurrido un error al obtener a los voluntarios de la emergencia. " + e.getMessage());
        }
    }

    public void deleteEmergencia(Integer idEmergencia) {
        try{
            if(emergenciaRepository.findById(idEmergencia) == null) throw new ApiErrorException("La emergencia no existe.");
            emergenciaRepository.delete(idEmergencia);
        }catch (Exception e){
            throw new ApiErrorException("Ha ocurrido un error al eliminar la emergencia. " + e.getMessage());
        }
    }

    public void addHabilidadEmergencia(Integer idEmergencia, Integer idHabilidad) {
        try{
            Emergencia emergencia = emergenciaRepository.findById(idEmergencia);
            if(emergencia == null) throw new ApiErrorException("La emergencia no existe.");
            if(habilidadRepository.findById(idHabilidad) == null) throw new ApiErrorException("La habilidad no existe");
            emergenciaRepository.saveEmeHabilidad(idEmergencia, idHabilidad);
        }catch(Exception e ){
            throw new ApiErrorException("Ha ocurrido un error al agregar la habilidad a la emergencia. " + e.getMessage());
        }
    }

    public void deleteHabilidadEmergencia(Integer idEmergencia, Integer idHabilidad) {
        try{
            Emergencia emergencia = emergenciaRepository.findById(idEmergencia);
            if(emergencia == null) throw new ApiErrorException("La emergencia no existe.");
            if(habilidadRepository.findById(idHabilidad) == null) throw new ApiErrorException("La habilidad no existe");
            emergenciaRepository.deleteEmeHabilidad(idEmergencia, idHabilidad);
        }catch(Exception e ){
            throw new ApiErrorException("Ha ocurrido un error al eliminar la habilidad a la emergencia. " + e.getMessage());
        }
    }

    public void updateHabilidadEmergencia(Integer idEmergencia, Integer idHabilidad, Integer newIdHabilidad) {
        try{
            Emergencia emergencia = emergenciaRepository.findById(idEmergencia);
            if(emergencia == null) throw new ApiErrorException("La emergencia no existe.");
            if(habilidadRepository.findById(idHabilidad) == null || habilidadRepository.findById(newIdHabilidad) == null) throw new ApiErrorException("Una de las habilidades no existe.");
            emergenciaRepository.setEmeHabilidad(idEmergencia, idHabilidad, newIdHabilidad);
        }catch(Exception e ){
            throw new ApiErrorException("Ha ocurrido un error al actualizar la habilidad a la emergencia. " + e.getMessage());
        }
    }

}
