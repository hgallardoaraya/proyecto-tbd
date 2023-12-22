package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Estado;
import cl.vol.app_voluntario.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class EstadoRepositoryImp implements EstadoRepository{
    @Autowired
    private Sql2o sql2o;

    @Override
    public void save(Estado estado) {
        try (Connection con = sql2o.beginTransaction()) {
            Integer id = con.createQuery("SELECT nextval('estado_seq')")
                    .executeScalar(Integer.class);
            String sql = "INSERT INTO estado (id_estado, descripcion) " +
                    "VALUES (:id_estado, :descripcion)";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_estado", id)
                    .addParameter("descripcion", estado.getDescripcion())
                    .executeUpdate()
                    .getResult();
            con.commit();
        }
    }

    @Override
    public Estado findByTareaId(Integer idTarea) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT e.* FROM estado e JOIN tarea t ON t.id_estado = e.id_estado AND t.id_tarea = :id_tarea";
            return con.createQuery(sql)
                    .addColumnMapping("id_estado", "id")
                    .addParameter("id_tarea", idTarea)
                    .executeAndFetchFirst(Estado.class);
        }
    }

    @Override
    public Estado findById(Integer idEstado) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT e.*  FROM estado e WHERE e.id_estado = :id_estado";
            return con.createQuery(sql)
                    .addColumnMapping("id_estado", "id")
                    .addParameter("id_estado", idEstado)
                    .executeAndFetchFirst(Estado.class);
        }
    }

    @Override
    public List<Estado> findAll() {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT e.*  FROM estado e";
            return con.createQuery(sql)
                    .addColumnMapping("id_estado", "id")
                    .executeAndFetch(Estado.class);
        }
    }

    @Override
    public void set(Estado estado){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "UPDATE estado " +
                    "SET descripcion = :descripcion " +
                    "WHERE id_estado = :id_estado";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_estado", estado.getId())
                    .addParameter("descripcion", estado.getDescripcion())
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void delete(Integer idEstado) {
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "DELETE FROM estado " +
                    "WHERE id_estado = :id_estado";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_estado", idEstado)
                    .executeUpdate();
            con.commit();
        }
    }
}
