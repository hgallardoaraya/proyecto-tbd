package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Habilidad;
import cl.vol.app_voluntario.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class HabilidadRepositoryImp implements  HabilidadRepository{
    @Autowired
    private Sql2o sql2o;

    @Override
    public Habilidad save(Habilidad habilidad) {
        try (Connection con = sql2o.beginTransaction()) {
            Integer id = con.createQuery("SELECT nextval('habilidad_seq')")
                    .executeScalar(Integer.class);
            String sql = "INSERT INTO habilidad (id_habilidad, descripcion) " +
                    "VALUES (:id_habilidad, :descripcion)";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addColumnMapping("id_habilidad", "id")
                    .addParameter("id_habilidad", id)
                    .addParameter("descripcion", habilidad.getDescripcion())
                    .executeUpdate()
                    .getResult();
            con.commit();
            return findById(id);
        }
    }

    @Override
    public Habilidad findById(Integer idHabilidad) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT h.id_habilidad, h.descripcion FROM habilidad h WHERE h.id_habilidad = :id_habilidad";
            return con.createQuery(sql)
                    .addColumnMapping("id_habilidad", "id")
                    .addParameter("id_habilidad", idHabilidad)
                    .executeAndFetchFirst(Habilidad.class);
        }
    }

    @Override
    public List<Habilidad> findAllByVoluntarioId(Integer idVoluntario) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT h.id_habilidad, h.descripcion " +
                    "FROM habilidad h " +
                    "JOIN vol_habilidad vh ON vh.id_voluntario = :id_voluntario AND vh.id_habilidad = h.id_habilidad";
            return con.createQuery(sql)
                    .addColumnMapping("id_habilidad", "id")
                    .addParameter("id_voluntario", idVoluntario)
                    .executeAndFetch(Habilidad.class);
        }
    }

    @Override
    public List<Habilidad> findAllByEmergenciaId(Integer idEmergencia) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT h.id_habilidad, h.descripcion " +
                    "FROM habilidad h " +
                    "JOIN eme_habilidad eh ON eh.id_emergencia = :id_emergencia AND eh.id_habilidad = h.id_habilidad";
            return con.createQuery(sql)
                    .addColumnMapping("id_habilidad", "id")
                    .addParameter("id_emergencia", idEmergencia)
                    .executeAndFetch(Habilidad.class);
        }
    }

    @Override
    public List<Habilidad> findAllByTareaId(Integer idTarea) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT h.id_habilidad, h.descripcion " +
                    "FROM tarea t " +
                    "JOIN tarea_habilidad th " +
                    "ON t.id_tarea = th.id_tarea " +
                    "JOIN eme_habilidad eh " +
                    "ON th.id_eme_habilidad = eh.id_eme_habilidad " +
                    "JOIN habilidad h " +
                    "ON h.id_habilidad = eh.id_habilidad " +
                    "AND t.id_tarea = :id_tarea";
            return con.createQuery(sql)
                    .addColumnMapping("id_habilidad", "id")
                    .addParameter("id_tarea", idTarea)
                    .executeAndFetch(Habilidad.class);
        }
    }

    @Override
    public void set(Habilidad habilidad){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "UPDATE habilidad " +
                    "SET descripcion = :descripcion " +
                    "WHERE id_habilidad = :id_habilidad";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_habilidad", habilidad.getId())
                    .addParameter("descripcion", habilidad.getDescripcion())
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public List<Habilidad> findAll(){
        try(Connection con = sql2o.open()){
            String sql = "SELECT id_habilidad, descripcion FROM habilidad ";
            return con.createQuery(sql)
                    .addColumnMapping("id_habilidad", "id")
                    .executeAndFetch(Habilidad.class);
        }
    }

    @Override
    public void delete(Integer idHabilidad){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "DELETE FROM habilidad " +
                    "WHERE id_habilidad = :id_habilidad ";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_habilidad", idHabilidad)
                    .executeUpdate();
            con.commit();
        }
    }
}
