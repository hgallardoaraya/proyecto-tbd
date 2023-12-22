package cl.vol.app_voluntario.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.sql2o.Connection;

public class TransactionUtil {
    public static void createTempTableWithUsername(Connection con, String consulta){
        con.createQuery("CREATE TEMPORARY TABLE temp_params(usuario varchar, consulta varchar);")
                .executeUpdate();
        con.createQuery("INSERT INTO temp_params(usuario, consulta) " +
                        "VALUES (:usuario, :consulta)")
                .addParameter("usuario", SecurityContextHolder.getContext().getAuthentication().getName())
                .addParameter("consulta", consulta)
                .executeUpdate();
    }
}
