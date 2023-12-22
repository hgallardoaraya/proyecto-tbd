package cl.vol.app_voluntario.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationUtil {
    public static Map<String, String> getValidationErrors(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        Map<String, String> errorMap = new HashMap<>();
        for(FieldError error : errors) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return errorMap;
    }

    public static boolean validateDates(Date fechaInicio, Date fechaFin){
            if(fechaInicio.compareTo(fechaFin) >= 0){
                return false;
            }

            return true;
    }
}
