package cl.vol.app_voluntario.repository;

import cl.vol.app_voluntario.model.Coordinador;
import cl.vol.app_voluntario.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

@Repository
public class CoordinadorRepositoryImp implements CoordinadorRepository{

    @Autowired
    private Sql2o sql2o;
    @Autowired
    private InstitucionRepository institucionRepository;

    @Override
    public Coordinador save(Coordinador coordinador) {
        try (Connection con = sql2o.beginTransaction()) {
            Integer id = con.createQuery("SELECT nextval('coordinador_seq')")
                    .executeScalar(Integer.class);
            String sql = "INSERT INTO coordinador (id_coordinador, id_usuario, id_institucion) " +
                    "VALUES (:id_coordinador, :id_usuario, :id_institucion)";
            TransactionUtil.createTempTableWithUsername(con, sql);
            con.createQuery(sql)
                    .addColumnMapping("id_coordinador", "id")
                    .addParameter("id_coordinador", id)
                    .addParameter("id_usuario", coordinador.getUsuario().getId())
                    .addParameter("id_institucion", coordinador.getInstitucion().getId())
                    .executeUpdate()
                    .getResult();
            con.commit();
            return findById(id);
        }
    }

    @Override
    public Coordinador findById(Integer idCoordinador) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT c.id_coordinador FROM coordinador c WHERE c.id_coordinador = :id_coordinador";
            Coordinador coordinador = con.createQuery(sql)
                    .addColumnMapping("id_coordinador", "id")
                    .addParameter("id_coordinador", idCoordinador)
                    .executeAndFetchFirst(Coordinador.class);
            if(coordinador == null) return null;
            coordinador.setInstitucion(institucionRepository.findByCoordinadorId(idCoordinador));
            return coordinador;
        }
    }

    @Override
    public Coordinador findByUserId(Integer idUsuario) {
        try (Connection con = sql2o.open()) {
            String sql = "SELECT c.id_coordinador FROM coordinador c WHERE c.id_usuario = :id_usuario;";
            Coordinador coordinador = con.createQuery(sql)
                    .addColumnMapping("id_coordinador", "id")
                    .addParameter("id_usuario", idUsuario)
                    .executeAndFetchFirst(Coordinador.class);
            if(coordinador == null) return null;
            coordinador.setInstitucion(institucionRepository.findByCoordinadorId(coordinador.getId()));
            return coordinador;
        }
    }

    @Override
    public void delete(Integer idCoordinador) {
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "DELETE FROM coordinador WHERE id_coordinador = :id_coordinador; ";

            TransactionUtil.createTempTableWithUsername(con, sql);
            Integer res = con.createQuery(sql)
                    .addParameter("id_coordinador", idCoordinador)
                    .executeUpdate()
                    .getResult();
            con.commit();
        }
    }

    @Override
    public void set(Coordinador coordinador) {
        try (Connection con = sql2o.beginTransaction()) {
            String sql = "UPDATE coordinador " +
                    "SET id_institucion = :id_institucion, " +
                    "id_usuario = :id_usuario " +
                    "WHERE id_coordinador = :id_coordinador; ";

            TransactionUtil.createTempTableWithUsername(con, sql);
            Integer res = con.createQuery(sql)
                    .addParameter("id_coordinador", coordinador.getId())
                    .addParameter("id_institucion", coordinador.getInstitucion().getId())
                    .addParameter("id_usuario", coordinador.getUsuario().getId())
                    .executeUpdate()
                    .getResult();
            con.commit();
        }
    }
}
