package cl.vol.app_voluntario.errors;

import cl.vol.app_voluntario.response.ApiResponse;
import cl.vol.app_voluntario.response.AuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<AuthenticationResponse> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity
                .badRequest()
                .body(AuthenticationResponse.builder()
                        .errorMessage(e.getMessage())
                        .build());
    }

    @ExceptionHandler(InvalidDatesException.class)
    public ResponseEntity<?> handleInvalidDatesException(InvalidDatesException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ApiResponse> handleApiErrorException(ApiErrorException e){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        return new ResponseEntity<>(
                ApiResponse.builder().
                        status(HttpStatus.BAD_REQUEST.value()).
                        messages(errorMap).
                        build(),
                HttpStatus.BAD_REQUEST
        );
    }
}

