package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Rol;
import cl.vol.app_voluntario.model.Usuario;
import cl.vol.app_voluntario.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;


import java.util.List;

@Repository
public class UsuarioRepositoryImp implements UsuarioRepository{
    @Autowired
    private Sql2o sql2o;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private CoordinadorRepository coordinadorRepository;

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Override
    public List<Usuario> findAll(){
        try (Connection con = sql2o.open()) {
            String sql = "SELECT id_usuario, nombre, apellido, email, ST_X(geom) AS longit, ST_Y(geom) AS latit FROM usuario";
            List<Usuario> usuarios = con.createQuery(sql)
                    .addColumnMapping("id_usuario", "id")
                    .addColumnMapping("latit", "latit")
                    .addColumnMapping("longit", "longit")
                    .executeAndFetch(Usuario.class);
            for (Usuario usuario : usuarios) {
                usuario.setRoles(rolRepository.findAllByUserId(usuario.getId()));
                usuario.setCoordinador(coordinadorRepository.findByUserId(usuario.getId()));
            }
            return usuarios;
        }
    }

    @Override
    public Usuario findByEmail(String email){
        try (Connection con = sql2o.open()) {
            String sql = "SELECT id_usuario, nombre, apellido, email, password FROM usuario WHERE email = :email";
            Usuario usuario = con.createQuery(sql)
                    .addColumnMapping("id_usuario", "id")
                    .addParameter("email", email)
                    .executeAndFetchFirst(Usuario.class);
            if(usuario == null) return null;
            usuario.setRoles(rolRepository.findAllByUserId(usuario.getId()));
            usuario.setCoordinador(coordinadorRepository.findByUserId(usuario.getId()));
            return usuario;
        }
    }

    @Override
    public Usuario save(Usuario usuario) {
        try (Connection con = sql2o.beginTransaction()) {
            Integer id = con.createQuery("SELECT nextval('usuario_seq')")
                    .executeScalar(Integer.class);
            String sql = "INSERT INTO usuario (id_usuario, nombre, apellido, email, password, geom) " +
                    "VALUES (:id_usuario, :nombre, :apellido, :email, :password, (SELECT ST_SetSRID(ST_MakePoint(:longit, :latit),4326)))";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addColumnMapping("id_usuario", "id")
                    .addParameter("id_usuario", id)
                    .addParameter("nombre", usuario.getNombre())
                    .addParameter("apellido", usuario.getApellido())
                    .addParameter("email", usuario.getEmail())
                    .addParameter("password", usuario.getPassword())
                    .addParameter("longit", usuario.getLongit())
                    .addParameter("latit", usuario.getLatit())
                    .executeUpdate();
            con.commit();
            return findById(id);
        }
    }

    @Override
    public Usuario saveUserRoles(Integer idUsuario, List<Rol> roles) {
        try (Connection con = sql2o.beginTransaction()) {
            for(Rol rol : roles){
                Integer idUsuarioRol = con.createQuery("SELECT nextval('usuario_rol_seq')")
                        .executeScalar(Integer.class);
                String sql = "INSERT INTO usuario_rol(id_usuario_rol, id_usuario, id_rol) " +
                        "VALUES (:id_usuario_rol, :id_usuario, :id_rol)";
                TransactionUtil.createTempTableWithUsername(con, sql);
                con.createQuery(sql)
                    .addParameter("id_usuario_rol", idUsuarioRol)
                    .addParameter("id_usuario", idUsuario)
                    .addParameter("id_rol", rol.getId())
                    .executeUpdate();
            }
            con.commit();
            return findById(idUsuario);
        }
    }

    @Override
    public void saveUserRol(Integer idUsuario, Integer idRol){
        try (Connection con = sql2o.beginTransaction()) {
            Integer idUsuarioRol = con.createQuery("SELECT nextval('usuario_rol_seq')")
                    .executeScalar(Integer.class);
            String sql = "INSERT INTO usuario_rol(id_usuario_rol, id_usuario, id_rol) " +
                    "VALUES (:id_usuario_rol, :id_usuario, :id_rol)";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_usuario_rol", idUsuarioRol)
                    .addParameter("id_usuario", idUsuario)
                    .addParameter("id_rol", idRol)
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void deleteUserRol(Integer idUsuario, Integer idRol){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "DELETE FROM usuario_rol " +
                    "WHERE id_usuario = :id_usuario AND id_rol = :id_rol ";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_usuario", idUsuario)
                    .addParameter("id_rol", idRol)
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public Usuario findById(Integer idUsuario) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT id_usuario, nombre, apellido, email, ST_X(geom) AS longit, ST_Y(geom) AS latit FROM usuario WHERE id_usuario = :id_usuario";
            Usuario usuario = con.createQuery(sql)
                    .addColumnMapping("id_usuario", "id")
                    .addColumnMapping("latit", "latit")
                    .addColumnMapping("longit", "longit")
                    .addParameter("id_usuario", idUsuario)
                    .executeAndFetchFirst(Usuario.class);
            if(usuario == null) return null;
            usuario.setRoles(rolRepository.findAllByUserId(usuario.getId()));
            usuario.setCoordinador(coordinadorRepository.findByUserId(usuario.getId()));
            usuario.setVoluntario(voluntarioRepository.findVoluntarioByUserId(usuario.getId()));
            System.out.println("usuario " + usuario);
            return usuario;
        }
    }

    @Override
    public List<Usuario> findAllByRoleId(Integer idRol){
        try (Connection con = sql2o.open()) {
            String sql = "SELECT u.id_usuario, u.nombre, u.apellido, u.email " +
                    "FROM usuario u " +
                    "JOIN usuario_rol ur ON u.id_usuario = ur.id_usuario " +
                    "JOIN rol r ON r.id_rol = ur.id_rol AND r.id_rol = :id_rol ";
            List<Usuario> usuarios = con.createQuery(sql)
                    .addColumnMapping("id_usuario", "id")
                    .addParameter("id_rol", idRol)
                    .executeAndFetch(Usuario.class);
            for (Usuario usuario : usuarios) {
                usuario.setRoles(rolRepository.findAllByUserId(usuario.getId()));
                usuario.setCoordinador(coordinadorRepository.findByUserId(usuario.getId()));
            }
            return usuarios;
        }
    }

    @Override
    public List<Usuario> findAllVoluntariosByEmergenciaId(Integer idEmergencia){
        try (Connection con = sql2o.open()) {
            String sql = "SELECT DISTINCT u.id_usuario, u.nombre, u.apellido, u.email, ST_X(u.geom) AS longit, ST_Y(u.geom) AS latit " +
                    "FROM usuario u " +
                    "JOIN usuario_rol ur ON u.id_usuario = ur.id_usuario " +
                    "JOIN rol r ON r.id_rol = ur.id_rol AND r.id_rol = 2 " +
                    "JOIN voluntario v ON u.id_usuario = v.id_usuario " +
                    "JOIN ranking ra ON ra.id_voluntario = v.id_voluntario " +
                    "JOIN tarea t ON ra.id_tarea = t.id_tarea " +
                    "JOIN emergencia e ON e.id_emergencia = t.id_emergencia " +
                    "AND e.id_emergencia = :id_emergencia";
            List<Usuario> usuarios = con.createQuery(sql)
                    .addColumnMapping("id_usuario", "id")
                    .addColumnMapping("latit", "latit")
                    .addColumnMapping("longit", "longit")
                    .addParameter("id_emergencia", idEmergencia)
                    .executeAndFetch(Usuario.class);
            for(Usuario usuario : usuarios) {
                System.out.println(usuario.getLatit() + " AND " + usuario.getLongit());
                usuario.setRoles(rolRepository.findAllByUserId(usuario.getId()));
            }
            return usuarios;
        }
    }

    @Override
    public void delete(Integer idUsuario){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "DELETE FROM usuario WHERE id_usuario = :id_usuario; ";

            TransactionUtil.createTempTableWithUsername(con, sql);
            Integer res = con.createQuery(sql)
                    .addParameter("id_usuario", idUsuario)
                    .executeUpdate()
                    .getResult();
            con.commit();
        }
    }

    @Override
    public void set(Usuario usuario){
        try (Connection con = sql2o.beginTransaction()) {
            System.out.println("Usuario " + usuario);
            String sql = "UPDATE usuario " +
                    "SET nombre = :nombre, " +
                    "apellido = :apellido, " +
                    "email = :email, " +
                    "password = :password, " +
                    "geom = (SELECT ST_SetSRID(ST_MakePoint(:longit, :latit),4326)) " +
                    "WHERE id_usuario = :id_usuario";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_usuario", usuario.getId())
                    .addParameter("nombre", usuario.getNombre())
                    .addParameter("apellido", usuario.getApellido())
                    .addParameter("email", usuario.getEmail())
                    .addParameter("password", usuario.getPassword())
                    .addParameter("longit", usuario.getLongit())
                    .addParameter("latit", usuario.getLatit())
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void deleteAllUserRoles(Integer idUsuario){
        try(Connection con = sql2o.beginTransaction()){
            String sql = "DELETE FROM usuario_rol " +
                    "WHERE id_usuario = :id_usuario";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_usuario", idUsuario)
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void setUserRol(Integer idUsuario, Integer idRol, Integer newIdRol){
        try(Connection con = sql2o.beginTransaction()){
            String sql = "UPDATE usuario_rol " +
                    "SET id_rol = :new_id_rol " +
                    "WHERE id_rol = :id_rol " +
                    "AND id_usuario = :id_usuario";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_usuario", idUsuario)
                    .addParameter("id_rol", idRol)
                    .addParameter("new_id_rol", newIdRol)
                    .executeUpdate();
            con.commit();
        }
    }
}
