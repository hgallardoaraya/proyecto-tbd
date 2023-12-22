package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Institucion;
import cl.vol.app_voluntario.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class InstitucionRepositoryImp implements  InstitucionRepository{
    @Autowired
    private Sql2o sql2o;

    @Override
    public void save(Institucion institucion) {
        try (Connection con = sql2o.beginTransaction()) {
            Integer id = con.createQuery("SELECT nextval('institucion_seq')")
                    .executeScalar(Integer.class);
            String sql = "INSERT INTO institucion (id_institucion, nombre, descripcion) " +
                    "VALUES (:id_institucion, :nombre, :descripcion)";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_institucion", id)
                    .addParameter("nombre", institucion.getNombre())
                    .addParameter("descripcion", institucion.getDescripcion())
                    .executeUpdate()
                    .getResult();
            con.commit();
        }
    }
    @Override
    public Institucion findById(Integer idInstitucion) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT * FROM institucion WHERE id_institucion = :id_institucion";
            return con.createQuery(sql)
                    .addColumnMapping("id_institucion", "id")
                    .addColumnMapping("nombre", "nombre")
                    .addColumnMapping("descripcion", "descripcion")
                    .addParameter("id_institucion", idInstitucion)
                    .executeAndFetchFirst(Institucion.class);
        }
    }

    @Override
    public Institucion findByEmergenciaId(Integer idEmergencia) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT i.* FROM emergencia e JOIN institucion i ON e.id_institucion = i.id_institucion AND e.id_emergencia = :id_emergencia";
            return con.createQuery(sql)
                    .addColumnMapping("id_institucion", "id")
                    .addColumnMapping("nombre", "nombre")
                    .addColumnMapping("descripcion", "descripcion")
                    .addParameter("id_emergencia", idEmergencia)
                    .executeAndFetchFirst(Institucion.class);
        }
    }

    @Override
    public Institucion findByCoordinadorId(Integer idCoordinador) {
        try(Connection con = sql2o.open()){
            String rolSql = "SELECT i.* FROM coordinador c JOIN institucion i ON c.id_institucion = i.id_institucion AND c.id_coordinador = :id_coordinador";
            return con.createQuery(rolSql)
                    .addColumnMapping("id_institucion", "id")
                    .addColumnMapping("nombre", "nombre")
                    .addColumnMapping("descripcion", "descripcion")
                    .addParameter("id_coordinador", idCoordinador)
                    .executeAndFetchFirst(Institucion.class);
        }
    }

    @Override
    public List<Institucion> findAll() {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT * FROM institucion";
            return con.createQuery(sql)
                    .addColumnMapping("id_institucion", "id")
                    .executeAndFetch(Institucion.class);
        }
    }

    @Override
    public void set(Institucion institucion){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "UPDATE institucion " +
                    "SET nombre = :nombre, " +
                    "descripcion = :descripcion " +
                    "WHERE id_institucion = :id_institucion";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_institucion", institucion.getId())
                    .addParameter("nombre", institucion.getNombre())
                    .addParameter("descripcion", institucion.getDescripcion())
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void delete(Integer idInstitucion){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "DELETE FROM institucion " +
                    "WHERE id_institucion = :id_institucion";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_institucion", idInstitucion)
                    .executeUpdate();
            con.commit();
        }
    }
}
