package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.errors.ApiErrorException;
import cl.vol.app_voluntario.model.Habilidad;
import cl.vol.app_voluntario.model.Tarea;
import cl.vol.app_voluntario.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class TareaRepositoryImp implements TareaRepository{
    @Autowired
    private Sql2o sql2o;
    @Autowired
    private EmergenciaRepository emergenciaRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private HabilidadRepository habilidadRepository;
    @Override
    public Tarea save(Tarea tarea) {
        try (Connection con = sql2o.beginTransaction()) {
            Integer id = con.createQuery("SELECT nextval('tarea_seq')")
                    .executeScalar(Integer.class);
            String sql = "INSERT INTO tarea (id_tarea, nombre, descripcion, cant_vol_requeridos, cant_vol_inscritos, fecha_inicio, fecha_fin, id_emergencia, id_estado, geom) " +
                    "VALUES (:id_tarea, :nombre, :descripcion, :cant_vol_requeridos, :cant_vol_inscritos, :fecha_inicio, :fecha_fin, :id_emergencia, :id_estado, (SELECT ST_SetSRID(ST_MakePoint(:longit, :latit),4326)))";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addColumnMapping("id_tarea", "id")
                    .addColumnMapping("cant_vol_requeridos", "voluntariosRequeridos")
                    .addColumnMapping("cant_vol_inscritos", "voluntariosInscritos")
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fecha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .addParameter("id_tarea", id)
                    .addParameter("nombre", tarea.getNombre())
                    .addParameter("descripcion", tarea.getDescripcion())
                    .addParameter("cant_vol_requeridos", tarea.getVoluntariosRequeridos())
                    .addParameter("cant_vol_inscritos", tarea.getVoluntariosInscritos())
                    .addParameter("fecha_inicio", tarea.getFechaInicio())
                    .addParameter("fecha_fin", tarea.getFechaFin())
                    .addParameter("id_emergencia", tarea.getEmergencia().getId())
                    .addParameter("id_estado", tarea.getEstado().getId())
                    .addParameter("longit", tarea.getLongit())
                    .addParameter("latit", tarea.getLatit())
                    .executeUpdate();
            con.commit();
            return findById(id);
        }
    }

    @Override
    public boolean saveTareaHabilidades(Integer idTarea, List<Habilidad> habilidades){
        try (Connection con = sql2o.beginTransaction()) {
            for(Habilidad habilidad : habilidades){
                Integer idEmeHabilidad = emergenciaRepository.findEmeHabilidadIdByHabilidadId(habilidad.getId());
                if(idEmeHabilidad == null) return false;
                Integer id = con.createQuery("SELECT nextval('tarea_habilidad_seq')")
                        .executeScalar(Integer.class);
                String sql = "INSERT INTO tarea_habilidad (id_tarea_habilidad, id_eme_habilidad, id_tarea) " +
                        "VALUES (:id_tarea_habilidad, :id_eme_habilidad, :id_tarea)";
                con.createQuery(sql)
                        .addParameter("id_tarea_habilidad", id)
                        .addParameter("id_eme_habilidad", idEmeHabilidad)
                        .addParameter("id_tarea", idTarea)
                        .executeUpdate();
            }
            con.commit();
            return true;
        }
    }

    @Override
    public Tarea findById(Integer idTarea) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT t.id_tarea, t.nombre, t.descripcion, t.cant_vol_requeridos, t.cant_vol_inscritos, t.fecha_inicio, t.fecha_fin, ST_X(t.geom) AS longit, ST_Y(t.geom) AS latit FROM tarea t WHERE t.id_tarea = :id_tarea";
            Tarea tarea = con.createQuery(sql)
                    .addColumnMapping("id_tarea", "id")
                    .addColumnMapping("cant_vol_requeridos", "voluntariosRequeridos")
                    .addColumnMapping("cant_vol_inscritos", "voluntariosInscritos")
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fecha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .addParameter("id_tarea", idTarea)
                    .executeAndFetchFirst(Tarea.class);
            if (tarea == null) return null;
            tarea.setEmergencia(emergenciaRepository.findByTareaId(tarea.getId()));
            tarea.setEstado(estadoRepository.findByTareaId(tarea.getId()));
            tarea.setHabilidades(habilidadRepository.findAllByTareaId(tarea.getId()));
            return tarea;
        }
    }

    @Override
    public List<Tarea> findAllByVoluntarioId(Integer idVoluntario) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT t.id_tarea, t.nombre, t.descripcion, t.cant_vol_requeridos, t.cant_vol_inscritos, t.fecha_inicio, t.fecha_fin, ST_X(t.geom) AS longit, ST_Y(t.geom) AS latit " +
                    "FROM tarea t " +
                    "JOIN ranking r ON r.id_voluntario = :id_voluntario AND r.id_tarea = t.id_tarea";
            List<Tarea> tareas = con.createQuery(sql)
                    .addColumnMapping("id_tarea", "id")
                    .addColumnMapping("cant_vol_requeridos", "voluntariosRequeridos")
                    .addColumnMapping("cant_vol_inscritos", "voluntariosInscritos")
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fecha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .addParameter("id_voluntario", idVoluntario)
                    .executeAndFetch(Tarea.class);
            for (Tarea tarea : tareas){
                tarea.setEmergencia(emergenciaRepository.findByTareaId(tarea.getId()));
                tarea.setEstado(estadoRepository.findByTareaId(tarea.getId()));
            }
            return tareas;
        }
    }

    @Override
    public List<Tarea> findAll() {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT id_tarea, nombre, descripcion, cant_vol_requeridos, cant_vol_inscritos, fecha_inicio, fecha_fin, ST_X(geom) AS longit, ST_Y(geom) AS latit FROM tarea";
            List<Tarea> tareas = con.createQuery(sql)
                    .addColumnMapping("id_tarea", "id")
                    .addColumnMapping("cant_vol_requeridos", "voluntariosRequeridos")
                    .addColumnMapping("cant_vol_inscritos", "voluntariosInscritos")
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fecha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .executeAndFetch(Tarea.class);
            for (Tarea tarea : tareas){
                tarea.setEstado(estadoRepository.findByTareaId(tarea.getId()));
            }
            return tareas;
        }
    }

    @Override
    public void set(Tarea tarea){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "UPDATE tarea " +
                    "SET descripcion = :descripcion, " +
                    "nombre = :nombre, " +
                    "cant_vol_requeridos = :cant_vol_requeridos, " +
                    "cant_vol_inscritos = :cant_vol_inscritos, " +
                    "fecha_inicio = :fecha_inicio, " +
                    "fecha_fin = :fecha_fin, " +
                    "id_emergencia = :id_emergencia, " +
                    "id_estado = :id_estado, " +
                    "geom = (SELECT ST_SetSRID(ST_MakePoint(:longit, :latit),4326)) " +
                    "WHERE id_tarea = :id_tarea";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addColumnMapping("id_tarea", "id")
                    .addColumnMapping("cant_vol_requeridos", "voluntariosRequeridos")
                    .addColumnMapping("cant_vol_inscritos", "voluntariosInscritos")
                    .addColumnMapping("fecha_inicio", "fechaInicio")
                    .addColumnMapping("fecha_fin", "fechaFin")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .addParameter("id_tarea", tarea.getId())
                    .addParameter("nombre", tarea.getNombre())
                    .addParameter("descripcion", tarea.getDescripcion())
                    .addParameter("cant_vol_requeridos", tarea.getVoluntariosRequeridos())
                    .addParameter("cant_vol_inscritos", tarea.getVoluntariosInscritos())
                    .addParameter("fecha_inicio", tarea.getFechaInicio())
                    .addParameter("fecha_fin", tarea.getFechaFin())
                    .addParameter("id_emergencia", tarea.getEmergencia().getId())
                    .addParameter("id_estado", tarea.getEstado().getId())
                    .addParameter("longit", tarea.getLongit())
                    .addParameter("latit", tarea.getLatit())
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void delete(Integer idTarea) {
        try (Connection con = sql2o.open()) {
            String sql = "DELETE FROM tarea WHERE id_tarea = :id_tarea; ";

            TransactionUtil.createTempTableWithUsername(con, sql);
            Integer res = con.createQuery(sql)
                    .addParameter("id_tarea", idTarea)
                    .executeUpdate()
                    .getResult();
        }

    }
    @Override
    public void deleteTareaHabilidad(Integer idTarea) {
        try (Connection con = sql2o.open()) {
            String sql = "DELETE FROM tarea_habilidad WHERE id_tarea = :id_tarea; ";

            TransactionUtil.createTempTableWithUsername(con, sql);
            Integer res = con.createQuery(sql)
                    .addParameter("id_tarea", idTarea)
                    .executeUpdate()
                    .getResult();
        }
    }

    @Override
    public List<Tarea> findAllByNombreRegion(String nombreRegion) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT t.id_tarea, t.nombre, ST_X(t.geom) AS longit, ST_Y(t.geom) AS latit " +
                    "FROM division_regional dr, tarea t " +
                    "WHERE dr.nom_reg = :nombre_region  " +
                    "AND ST_Contains(dr.geom, t.geom) ";
            List<Tarea> tareas = con.createQuery(sql)
                    .addColumnMapping("id_tarea", "id")
                    .addColumnMapping("nombre", "nombre")
                    .addColumnMapping("longit", "longit")
                    .addColumnMapping("latit", "latit")
                    .addParameter("nombre_region", nombreRegion)
                    .executeAndFetch(Tarea.class);
            return tareas;
        }
    }

    @Override
    public void deleteRankingByVoluntarioId(Integer idVoluntario){
        try (Connection con = sql2o.open()) {
            String sql = "DELETE FROM ranking WHERE id_voluntario = :id_voluntario; ";

            TransactionUtil.createTempTableWithUsername(con, sql);
            Integer res = con.createQuery(sql)
                    .addParameter("id_voluntario", idVoluntario)
                    .executeUpdate()
                    .getResult();
        }
    }

    @Override
    public void saveTareaHabilidad(Integer idTarea, Integer idHabilidad){
        try (Connection con = sql2o.beginTransaction()) {
            Integer idEmeHabilidad = emergenciaRepository.findEmeHabilidadIdByHabilidadId(idHabilidad);
            if(idEmeHabilidad == null) throw new ApiErrorException("La habilidad no se corresponde con la emergencia de esta tarea");
            Integer id = con.createQuery("SELECT nextval('tarea_habilidad_seq')")
                    .executeScalar(Integer.class);
            String sql = "INSERT INTO tarea_habilidad (id_tarea_habilidad, id_eme_habilidad, id_tarea) " +
                    "VALUES (:id_tarea_habilidad, " +
                    ":id_eme_habilidad" +
                    ":id_tarea)";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_tarea_habilidad", id)
                    .addParameter("id_habilidad", idHabilidad)
                    .addParameter("id_eme_habilidad", idEmeHabilidad)
                    .addParameter("id_tarea", idTarea)
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void deleteTareaHabilidad(Integer idTarea, Integer idHabilidad){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "DELETE FROM tarea_habilidad " +
                    "WHERE id_tarea = :id_tarea " +
                    "AND id_eme_habilidad = (SELECT eh.id_eme_habilidad FROM eme_habilidad eh WHERE eh.id_habilidad = :id_habilidad)  ";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_habilidad", idHabilidad)
                    .addParameter("id_tarea", idTarea)
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void setTareaHabilidad(Integer idTarea, Integer idHabilidad, Integer newIdHabilidad){
        try (Connection con = sql2o.beginTransaction()) {
            Integer idEmeHabilidad = emergenciaRepository.findEmeHabilidadIdByHabilidadId(idHabilidad);
            if(idEmeHabilidad == null) throw new ApiErrorException("La habilidad no se corresponde con la emergencia de esta tarea");
            String sql = "UPDATE tarea_habilidad " +
                    "SET id_eme_habilidad = (SELECT eh.id_eme_habilidad FROM eme_habilidad eh WHERE eh.id_habilidad = :new_id_habilidad) " +
                    "WHERE id_tarea = :id_tarea AND id_eme_habilidad = :id_eme_habilidad";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addParameter("id_tarea", idTarea)
                    .addParameter("id_eme_habilidad", idEmeHabilidad)
                    .addParameter("new_id_habilidad", newIdHabilidad)
                    .executeUpdate();
            con.commit();
        }
    }

    @Override
    public void deleteAllTareaHabilidad(Integer idTarea){
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "DELETE FROM tarea_habilidad " +
                    "WHERE id_tarea = :id_tarea";
            con.createQuery(sql)
                    .addParameter("id_tarea", idTarea)
                    .executeUpdate();
            con.commit();
        }
    }
}
