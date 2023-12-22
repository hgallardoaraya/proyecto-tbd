package cl.vol.app_voluntario.service;

import cl.vol.app_voluntario.errors.ApiErrorException;
import cl.vol.app_voluntario.model.Usuario;
import cl.vol.app_voluntario.repository.CoordinadorRepository;
import cl.vol.app_voluntario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinadorService {
    private final UsuarioRepository usuarioRepository;
    private final CoordinadorRepository coordinadorRepository;
    
    public List<Usuario> getCoordinadores(){
        try{
            return usuarioRepository.findAllByRoleId(1);
        }catch (Exception e){
            throw new ApiErrorException("No se han encontrado coordinadores.");
        }
    }
}