package cl.vol.app_voluntario.controller;

import cl.vol.app_voluntario.model.Rol;
import cl.vol.app_voluntario.request.*;
import cl.vol.app_voluntario.response.ApiResponse;
import cl.vol.app_voluntario.service.RolService;
import cl.vol.app_voluntario.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    //CREATE
    @PostMapping("/roles")
    public ResponseEntity<?> createRol(
            @Valid @RequestBody CreateRolRequest request,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>
                    (new ApiResponse().builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .messages(ValidationUtil.getValidationErrors(bindingResult))
                            .build(),
                            HttpStatus.BAD_REQUEST);
        };

        rolService.createRol(request);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Rol creado con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getInstituciones(){
        return new ResponseEntity<>(rolService.getRoles(), HttpStatus.FOUND);
    }

    //UPDATE
    @PutMapping("/roles/{id}")
    public ResponseEntity<?> updateRol(@PathVariable Integer id,
                                        @Valid @RequestBody Rol request){
        rolService.updateRol(id, request);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Rol editado con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    };

    @DeleteMapping("/roles/{idRol}")
    public ResponseEntity<?> deleteRol(@PathVariable Integer idRol){
        rolService.deleteRol(idRol);

        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Rol eliminado con éxito.");

        return new ResponseEntity<>
            (new ApiResponse().builder()
                    .status(HttpStatus.OK.value())
                    .messages(messages)
                    .build(),
                    HttpStatus.OK);
    }
}
