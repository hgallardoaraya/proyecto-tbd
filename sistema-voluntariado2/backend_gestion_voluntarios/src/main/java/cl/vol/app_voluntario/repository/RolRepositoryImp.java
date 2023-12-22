package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Rol;
import cl.vol.app_voluntario.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class RolRepositoryImp implements RolRepository{

    @Autowired
    private Sql2o sql2o;

    public void save(Rol rol){
        try (Connection con = sql2o.beginTransaction()) {
            Integer id = con.createQuery("SELECT nextval('rol_seq')")
                    .executeScalar(Integer.class);
            String sql = "INSERT INTO rol (id_rol, nombre) " +
                    "VALUES (:id_rol, :nombre)";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_rol", id)
                    .addParameter("nombre", rol.getNombre())
                    .executeUpdate()
                    .getResult();
            con.commit();
        };
    }

    public List<Rol> findAll(){
        try (Connection con = sql2o.open()) {
            String sql = "SELECT * FROM rol";
            return con.createQuery(sql)
                    .addColumnMapping("id_rol", "id")
                    .executeAndFetch(Rol.class);
        }
    }
    @Override
    public Rol findById(int idRol) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT * FROM rol WHERE id_rol = :id_rol";
            return con.createQuery(sql)
                    .addColumnMapping("id_rol", "id")
                    .addParameter("id_rol", idRol)
                    .executeAndFetchFirst(Rol.class);
        }
    }

    @Override
    public List<Rol> findAllByUserId(int idUsuario) {
        try(Connection con = sql2o.open()){
            String rolSql = "SELECT r.* FROM rol r INNER JOIN usuario_rol ur ON r.id_rol = ur.id_rol WHERE ur.id_usuario = :id_usuario";
            return con.createQuery(rolSql)
                    .addColumnMapping("id_rol", "id")
                    .addParameter("id_usuario", idUsuario)
                    .executeAndFetch(Rol.class);
        }
    }

    @Override
    public void set(Rol rol){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "UPDATE rol " +
                    "SET nombre = :nombre " +
                    "WHERE id_rol = :id_rol";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_rol", rol.getId())
                    .addParameter("nombre", rol.getNombre())
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void delete(Integer idRol){
        try(Connection con = sql2o.beginTransaction()){
            String sql = "DELETE FROM rol WHERE id_rol = :id_rol";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_rol", idRol)
                    .executeUpdate();
            con.commit();
        }
    }
}
