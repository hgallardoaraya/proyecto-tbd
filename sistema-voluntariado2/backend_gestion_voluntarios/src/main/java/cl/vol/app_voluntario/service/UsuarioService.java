package cl.vol.app_voluntario.service;

import cl.vol.app_voluntario.config.JwtService;
import cl.vol.app_voluntario.errors.*;
import cl.vol.app_voluntario.model.*;
import cl.vol.app_voluntario.repository.*;
import cl.vol.app_voluntario.request.AuthenticationRequest;
import cl.vol.app_voluntario.request.RegisterRequest;
import cl.vol.app_voluntario.request.UpdateUsuarioRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final InstitucionRepository institucionRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final TareaRepository tareaRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public List<Usuario> getUsuarios(){
        return usuarioRepository.findAll();
    }

    public void createUsuario(RegisterRequest request) {
        try{
            if(!request.isVoluntario() && !request.isCoordinador()){
                throw new ApiErrorException("No ha seleccionado el tipo de usuario a crear.");
            }
            if(request.isCoordinador() && institucionRepository.findById(request.getIdInstitucion()) == null){
                throw new ApiErrorException("La institución seleccionada no existe.");
            }
            if(usuarioRepository.findByEmail(request.getEmail()) != null){
                throw new ApiErrorException("El correo electronico ya existe.");
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(request.getNombre());
            usuario.setApellido(request.getApellido());
            usuario.setEmail(request.getEmail());
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            usuario.setLatit(request.getLatit());
            usuario.setLongit(request.getLongit());
            Usuario usuarioBD = usuarioRepository.save(usuario);

            //guardar roles
            List<Rol> roles = new ArrayList<>();

            if(request.isVoluntario()){
                Voluntario voluntario = new Voluntario();
                voluntario.setUsuario(usuarioBD);
                voluntario.setHabilidades(new ArrayList<>());
                voluntario.setTareas(new ArrayList<>());
                voluntarioRepository.save(voluntario);
                roles.add(rolRepository.findById(2));
            }

            if(request.isCoordinador()){
                Coordinador coordinador = new Coordinador();
                coordinador.setUsuario(usuarioBD);
                coordinador.setInstitucion(institucionRepository.findById(request.getIdInstitucion()));
                coordinadorRepository.save(coordinador);
                roles.add(rolRepository.findById(1));
            }

           usuarioBD = usuarioRepository.saveUserRoles(usuarioBD.getId(), roles);
        }catch(Exception e){
            throw new ApiErrorException("Error al crear usuario " + e.getMessage());
        }
    }
    public Usuario getUsuario(int id) {
        return usuarioRepository.findById(id);
    }
    public List<Usuario> getAllVoluntarios(){ return usuarioRepository.findAllByRoleId(2);}
    public String authentication(AuthenticationRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail());

        System.out.println("AUTH EMAIL = " + usuario);

        if(usuario == null){
            throw new ApiErrorException("El usuario no existe.");
        }

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Rol rol = rolRepository.findById(request.getIdRol());
        Map<String, Object> extraClaims = jwtService.createExtraClaimWithIdAndRol(usuario.getId(), rol);
        return jwtService.generateToken(extraClaims, usuario);
    }

    public void deleteUsuario(Integer id) {
        try{
            Usuario usuario = usuarioRepository.findById(id);
            if(usuario == null) throw new ApiErrorException("El usuario no existe");

            usuarioRepository.delete(id);

        }catch(Exception e){
            throw new ApiErrorException("El usuario no ha podido ser eliminado " + e.getMessage());
        }
    }

    public void updateUsuario(Integer id, UpdateUsuarioRequest newUsuario) {
        try{
            if(newUsuario.isCoordinador() && institucionRepository.findById(newUsuario.getIdInstitucion()) == null){
                throw new ApiErrorException("La institución seleccionada no existe.");
            }
            else if(usuarioRepository.findByEmail(newUsuario.getEmail()) != null){
                throw new ApiErrorException("El correo electronico ya existe.");
            }

            Usuario usuario = usuarioRepository.findById(id);
            if(usuario == null) throw new ApiErrorException("El usuario no existe");
            if(newUsuario.getNombre() != null){
                usuario.setNombre(newUsuario.getNombre());
            }
            if(newUsuario.getApellido() != null){
                usuario.setApellido(newUsuario.getApellido());
            }
            if(newUsuario.getEmail() != null){
                usuario.setEmail(newUsuario.getEmail());
            }
            if(newUsuario.getPassword() != null){
                usuario.setPassword(passwordEncoder.encode(newUsuario.getPassword()));
            }
            if(newUsuario.getLatit() != null && newUsuario.getLongit() != null){
                usuario.setLatit(newUsuario.getLatit());
                usuario.setLongit(newUsuario.getLongit());
            }
            usuarioRepository.set(usuario);

            System.out.println(" newusuari " + newUsuario);
            if(newUsuario.isCoordinador() && usuario.getCoordinador() == null){
                System.out.println("ENTRA");
                Coordinador coordinador = new Coordinador();
                coordinador.setUsuario(usuario);
                coordinador.setInstitucion(institucionRepository.findById(newUsuario.getIdInstitucion()));
                coordinador = coordinadorRepository.save(coordinador);
                usuarioRepository.saveUserRol(usuario.getId(), 1);
            }else if(!newUsuario.isCoordinador() && usuario.getCoordinador() != null){
                coordinadorRepository.delete(usuario.getCoordinador().getId());
                usuarioRepository.deleteUserRol(usuario.getId(), 1);
            }

            usuario.setVoluntario(voluntarioRepository.findVoluntarioByUserId(usuario.getId()));
            if(newUsuario.isVoluntario() && usuario.getVoluntario() == null){
                Voluntario voluntario = new Voluntario();
                voluntario.setUsuario(usuario);
                voluntario.setHabilidades(new ArrayList<>());
                voluntario.setTareas(new ArrayList<>());
                voluntario = voluntarioRepository.save(voluntario);
                usuarioRepository.saveUserRol(usuario.getId(), 2);
            }else if(!newUsuario.isVoluntario() && usuario.getVoluntario() != null){
                voluntarioRepository.delete(usuario.getVoluntario().getId());
                usuarioRepository.deleteUserRol(usuario.getId(), 2);
            }

        }catch(Exception e){
            throw new ApiErrorException("No se pudo actualizar el usuario " + e.getMessage());
        }
    }

    public List<Rol> getRolesUsuario(Integer idUsuario){
        try{
            return rolRepository.findAllByUserId(idUsuario);
        }catch (Exception e){
            throw new ApiErrorException("Hubo un error al leer los roles del usuario. " + e.getMessage());
        }
    }

    public void addRolUsuario(Integer idUsuario, Integer idRol) {
        try{
            if(usuarioRepository.findById(idUsuario) == null) throw new ApiErrorException("El usuario no existe.");
            if(rolRepository.findById(idRol) == null) throw new ApiErrorException("El rol no existe.");
            usuarioRepository.saveUserRol(idUsuario, idRol);
        }catch (Exception e){
            throw new ApiErrorException("Hubo un error al añadir un rol al usuario. " + e.getMessage());
        }
    }

    public void updateRolUsuario(Integer idUsuario, Integer idRol, Integer newIdRol){
        try{
            usuarioRepository.setUserRol(idUsuario, idRol, newIdRol);
        }catch(Exception e){
            throw new ApiErrorException("Hubo un error al actualizar el rol del usuario. " + e.getMessage());
        }
    }

    public void deleteRolUsuario(Integer idUsuario, Integer idRol) {
        try{
            usuarioRepository.deleteUserRol(idUsuario, idRol);
        }catch (Exception e){
            throw new ApiErrorException("Hubo un error al eliminar el rol del usuario. " + e.getMessage());
        }
    }
}
