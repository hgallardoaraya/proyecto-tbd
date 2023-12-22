package cl.vol.app_voluntario.service;

import cl.vol.app_voluntario.errors.ApiErrorException;
import cl.vol.app_voluntario.model.Habilidad;
import cl.vol.app_voluntario.model.Tarea;
import cl.vol.app_voluntario.model.Usuario;
import cl.vol.app_voluntario.repository.*;
import cl.vol.app_voluntario.request.CreateVolTareaRequest;
import cl.vol.app_voluntario.request.UpdateHabilidadVoluntarioRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoluntarioService {
    @Autowired
    private final VoluntarioRepository voluntarioRepository;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final TareaRepository tareaRepository;

    @Autowired
    private final EmergenciaRepository emergenciaRepository;

    @Autowired
    private final HabilidadRepository habilidadRepository;


    public List<Habilidad> getHabilidadesVoluntario(Integer idUsuario) {
        try{
            Usuario usuario = usuarioRepository.findById(idUsuario);
            if(usuario == null) throw new ApiErrorException("El usuario no existe.");
            if(usuario.getVoluntario() == null) throw new ApiErrorException("El usuario no es un voluntario");
            return habilidadRepository.findAllByVoluntarioId(usuario.getVoluntario().getId());
        }catch(Exception e ){
            throw new ApiErrorException("No se pudo obtener las tareas del voluntario. " + e.getMessage());
        }
    }

    public void addHabilidadVoluntario(Integer idUsuario, Integer idHabilidad){
        try{
            Usuario usuario = usuarioRepository.findById(idUsuario);
            if(usuario == null) throw new ApiErrorException("El usuario no existe.");
            if(usuario.getVoluntario() == null) throw new ApiErrorException("El usuario no es un voluntario");
            if(habilidadRepository.findById(idHabilidad) == null) throw new ApiErrorException("La habilidad no existe");
            voluntarioRepository.saveVolHabilidad(usuario.getVoluntario().getId(), idHabilidad);
        }catch(Exception e ){
            throw new ApiErrorException("No se pudo agregar la habilidad al voluntario. " + e.getMessage());
        }
    }

    public void deleteHabilidadVoluntario(Integer idUsuario, Integer idHabilidad) {
        try{
            Usuario usuario = usuarioRepository.findById(idUsuario);
            if(usuario == null) throw new ApiErrorException("El usuario no existe.");
            if(usuario.getVoluntario() == null) throw new ApiErrorException("El usuario no es un voluntario");
            if(habilidadRepository.findById(idHabilidad) == null) throw new ApiErrorException("La habilidad no existe");
            voluntarioRepository.deleteVolHabilidad(usuario.getVoluntario().getId(), idHabilidad);
        }catch (Exception e){
            throw new ApiErrorException("No se pudo borrar la habilidad al voluntario. " + e.getMessage());
        }
    }

    public void updateHabilidadVoluntario(Integer idUsuario, Integer idHabilidad, Integer newIdHabilidad) {
        try{
            Usuario usuario = usuarioRepository.findById(idUsuario);
            if(usuario == null) throw new ApiErrorException("El usuario no existe.");
            if(usuario.getVoluntario() == null) throw new ApiErrorException("El usuario no es un voluntario");
            if(habilidadRepository.findById(idHabilidad) == null || habilidadRepository.findById(newIdHabilidad) == null) throw new ApiErrorException("Una de las habilidades no existe");
            voluntarioRepository.setVolHabilidad(usuario.getVoluntario().getId(), idHabilidad, newIdHabilidad);
        }catch (Exception e){
            throw new ApiErrorException("No se pudo actualizar la habilidad al voluntario. " + e.getMessage());
        }
    }
    public List<Usuario> getVoluntarios() {
        try{
            return usuarioRepository.findAllByRoleId(2);
        }catch (Exception e){
            throw new ApiErrorException("No se pudo obtener los voluntarios. " + e.getMessage());
        }
    }

    public void addTareaVoluntario(Integer idUsuario, Integer idTarea, CreateVolTareaRequest request) {
        try{
            Usuario usuario = usuarioRepository.findById(idUsuario);
            if(tareaRepository.findById(idTarea) == null) throw new ApiErrorException("La tarea no existe.");
            if(voluntarioRepository.findById(usuario.getVoluntario().getId()) == null) throw new ApiErrorException("El voluntario no existe.");
            voluntarioRepository.saveVolTarea(usuario.getVoluntario().getId(), idTarea, request);
        }catch (Exception e){
            throw new ApiErrorException("No se pudo asignar la tarea al voluntario. " + e.getMessage());
        }
    }

    public void deleteTareaVoluntario(Integer idUsuario, Integer idTarea) {
        try{
            Usuario usuario = usuarioRepository.findById(idUsuario);
            if(usuario == null) throw new ApiErrorException("El usuario no existe.");
            if(usuario.getVoluntario() == null) throw new ApiErrorException("El usuario no es un voluntario");
            if(tareaRepository.findById(idTarea) == null) throw new ApiErrorException("La tarea no existe");
            voluntarioRepository.deleteVolHabilidad(usuario.getVoluntario().getId(), idTarea);
        }catch (Exception e){
            throw new ApiErrorException("No se pudo borrar la tarea al voluntario. " + e.getMessage());
        }
    }

    public void updateTareaVoluntario(Integer idUsuario, Integer idTarea, Integer newIdTarea) {
        try{
            Usuario usuario = usuarioRepository.findById(idUsuario);
            if(usuario == null) throw new ApiErrorException("El usuario no existe.");
            if(usuario.getVoluntario() == null) throw new ApiErrorException("El usuario no es un voluntario");
            if(tareaRepository.findById(idTarea) == null || tareaRepository.findById(newIdTarea) == null) throw new ApiErrorException("Una de las tareas no existe");
            voluntarioRepository.setVolTarea(usuario.getVoluntario().getId(), idTarea, newIdTarea);
        }catch (Exception e){
            throw new ApiErrorException("No se pudo actualizar la tarea al voluntario. " + e.getMessage());
        }
    }

    public List<Tarea> getTareasVoluntario(Integer idUsuario) {
        try{
            Usuario usuario = usuarioRepository.findById(idUsuario);
            if(usuario == null) throw new ApiErrorException("El usuario no existe.");
            if(usuario.getVoluntario() == null) throw new ApiErrorException("El usuario no es un voluntario");
            return tareaRepository.findAllByVoluntarioId(usuario.getVoluntario().getId());
        }catch (Exception e){
            throw new ApiErrorException("No se pudo obtener las tareas del voluntario. " + e.getMessage());
        }
    }
}
