package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Emergencia;
import cl.vol.app_voluntario.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class EmergenciaRepositoryImp implements EmergenciaRepository{
    @Autowired
    private Sql2o sql2o;
    @Autowired
    private InstitucionRepository institucionRepository;
    @Autowired
    private HabilidadRepository habilidadRepository;
    @Override
    public Emergencia save(Emergencia emergencia) {
        try (Connection con = sql2o.beginTransaction()) {
            Integer id = con.createQuery("SELECT nextval('emergencia_seq')")
                    .executeScalar(Integer.class);

            String sql = "INSERT INTO emergencia (id_emergencia, nombre, descripcion, fecha_inicio, fecha_fin, id_institucion, geom ) " +
                    "VALUES (:id_emergencia, :nombre, :descripcion, :fecha_inicio, :fecha_fin, :id_institucion, (SELECT ST_SetSRID(ST_MakePoint(:longit, :latit),4326)))";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addColumnMapping("id_emergencia", "id")
                    .addColumnMapping("nombre", "nombre")
                    .addColumnMapping("descripcion", "descripcion")
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fecha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .addParameter("id_emergencia", id)
                    .addParameter("nombre", emergencia.getNombre())
                    .addParameter("descripcion", emergencia.getDescripcion())
                    .addParameter("fecha_inicio", emergencia.getFechaInicio())
                    .addParameter("fecha_fin", emergencia.getFechaFin())
                    .addParameter("id_institucion", emergencia.getInstitucion().getId())
                    .addParameter("longit", emergencia.getLongit())
                    .addParameter("latit", emergencia.getLatit())
                    .executeUpdate()
                    .getResult();
            con.commit();
            return findById(id);
        }
    }

    @Override
    public Emergencia findById(Integer idEmergencia) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT e.id_emergencia, e.nombre, e.descripcion, e.fecha_inicio, e.fecha_fin, ST_X(e.geom) AS longit, ST_Y(e.geom) AS latit FROM emergencia e WHERE e.id_emergencia = :id_emergencia";
            Emergencia emergencia = con.createQuery(sql)
                    .addColumnMapping("id_emergencia", "id")
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fecha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .addParameter("id_emergencia", idEmergencia)
                    .executeAndFetchFirst(Emergencia.class);
            if(emergencia == null) return null;
            emergencia.setInstitucion(institucionRepository.findByEmergenciaId(idEmergencia));
            return emergencia;
        }
    }

    @Override
    public List<Emergencia> findAll() {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT id_emergencia, nombre, descripcion, fecha_inicio, fecha_fin, ST_X(geom) AS longit, ST_Y(geom) AS latit FROM emergencia";
            List<Emergencia> emergencias = con.createQuery(sql)
                    .addColumnMapping("id_emergencia", "id" )
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fecha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .executeAndFetch(Emergencia.class);
            for (Emergencia emergencia : emergencias) {
                emergencia.setInstitucion(institucionRepository.findByEmergenciaId(emergencia.getId()));
                emergencia.setHabilidades(habilidadRepository.findAllByEmergenciaId(emergencia.getId()));
            }
            return emergencias;
        }
    }

    @Override
    public Emergencia findByTareaId(Integer idTarea) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT e.id_emergencia, e.nombre, e.descripcion, e.fecha_inicio, e.fecha_fin, ST_X(e.geom) AS longit, ST_Y(e.geom) AS latit FROM emergencia e JOIN tarea t ON t.id_emergencia = e.id_emergencia AND t.id_tarea = :id_tarea";
            Emergencia emergencia = con.createQuery(sql)
                    .addColumnMapping("id_emergencia", "id" )
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fecha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .addParameter("id_tarea", idTarea)
                    .executeAndFetchFirst(Emergencia.class);
            if(emergencia == null) return null;
            //ACA FALTAN LAS HABILIDADES Y TAREAS
            emergencia.setInstitucion(institucionRepository.findByEmergenciaId(emergencia.getId()));
            return emergencia;
        }
    }

    @Override
    public Integer findEmeHabilidadIdByHabilidadId(Integer idHabilidad){
        try (Connection con = sql2o.open()) {
            String sql = "SELECT eh.id_eme_habilidad FROM eme_habilidad eh WHERE eh.id_habilidad = :id_habilidad";
            return con.createQuery(sql)
                    .addParameter("id_habilidad", idHabilidad)
                    .executeScalar(Integer.class);
        }
    }

    @Override
    public List<Emergencia> findAllByHabilidadId(Integer idHabilidad){
        try (Connection con = sql2o.open()) {
            String sql = "SELECT e.id_emergencia, e.nombre, e.descripcion, e.fecha_inicio, e.fecha_fin, ST_X(e.geom) AS longit, ST_Y(e.geom) AS latit " +
                    "FROM emergencia e " +
                    "JOIN eme_habilidad eh ON e.id_emergencia = eh.id_emergencia AND eh.id_habilidad = :id_habilidad";
            List<Emergencia> emergencias = con.createQuery(sql)
                    .addColumnMapping("id_emergencia", "id" )
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fecha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .addParameter("id_habilidad", idHabilidad)
                    .executeAndFetch(Emergencia.class);
            for (Emergencia emergencia : emergencias) {
                emergencia.setInstitucion(institucionRepository.findByEmergenciaId(emergencia.getId()));
                emergencia.setHabilidades(habilidadRepository.findAllByEmergenciaId(emergencia.getId()));
            }
            return emergencias;
        }
    }

    @Override
    public void set(Emergencia emergencia){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "UPDATE emergencia " +
                    "SET descripcion = :descripcion, " +
                    "nombre = :nombre, " +
                    "fecha_inicio = :fecha_inicio, " +
                    "fecha_fin = :fecha_fin, " +
                    "id_institucion = :id_institucion, " +
                    "geom = (SELECT ST_SetSRID(ST_MakePoint(:longit, :latit),4326)) " +
                    "WHERE id_emergencia = :id_emergencia";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addColumnMapping("id_emergencia", "id")
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fec.ha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .addParameter("id_emergencia", emergencia.getId())
                    .addParameter("nombre", emergencia.getNombre())
                    .addParameter("descripcion", emergencia.getDescripcion())
                    .addParameter("fecha_inicio", emergencia.getFechaInicio())
                    .addParameter("fecha_fin", emergencia.getFechaFin())
                    .addParameter("id_institucion", emergencia.getInstitucion().getId())
                    .addParameter("longit", emergencia.getLongit())
                    .addParameter("latit", emergencia.getLatit())
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void delete(Integer idEmergencia){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "DELETE FROM emergencia " +
                    "WHERE id_emergencia = :id_emergencia";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_emergencia", idEmergencia)
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void saveEmeHabilidad(Integer idEmergencia, Integer idHabilidad){
        try (Connection con = sql2o.beginTransaction()) {
            Integer id = con.createQuery("SELECT nextval('eme_habilidad_seq')")
                    .executeScalar(Integer.class);
            String sql = "INSERT INTO eme_habilidad (id_eme_habilidad, id_emergencia, id_habilidad) " +
                    "VALUES(:id_eme_habilidad, :id_emergencia, :id_habilidad) ";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_eme_habilidad", id)
                    .addParameter("id_emergencia", idEmergencia)
                    .addParameter("id_habilidad", idHabilidad)
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void deleteEmeHabilidad(Integer idEmergencia, Integer idHabilidad){
        try(Connection con = sql2o.beginTransaction()){
            String sql = "DELETE FROM eme_habilidad " +
                    " WHERE id_emergencia = :id_emergencia AND id_habilidad = :id_habilidad";
            con.createQuery(sql)
                    .addParameter("id_emergencia", idEmergencia)
                    .addParameter("id_habilidad", idHabilidad)
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void setEmeHabilidad(Integer idEmergencia, Integer idHabilidad, Integer newIdHabilidad){
        try(Connection con = sql2o.beginTransaction()){
            String sql = "UPDATE eme_habilidad " +
                    "SET id_habilidad = :new_id_habilidad " +
                    "WHERE id_emergencia = :id_emergencia AND id_habilidad = :id_habilidad";
            con.createQuery(sql)
                    .addParameter("id_emergencia", idEmergencia)
                    .addParameter("id_habilidad", idHabilidad)
                    .addParameter("new_id_habilidad", newIdHabilidad)
                    .executeUpdate();
            con.commit();
        }
    }
}
