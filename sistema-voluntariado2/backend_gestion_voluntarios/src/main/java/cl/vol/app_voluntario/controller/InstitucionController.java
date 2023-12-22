package cl.vol.app_voluntario.controller;

import cl.vol.app_voluntario.model.Institucion;
import cl.vol.app_voluntario.request.CreateInstitucionRequest;
import cl.vol.app_voluntario.response.ApiResponse;
import cl.vol.app_voluntario.service.InstitucionService;
import cl.vol.app_voluntario.util.ValidationUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class InstitucionController {

    private final InstitucionService institucionService;

    //CREATE
    @PostMapping("/instituciones")
    public ResponseEntity<?> createInstitucion(
            @Valid @RequestBody CreateInstitucionRequest request,
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

        institucionService.createInstitucion(request);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Institución creada con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    }

    //READ ALL
    @GetMapping("/instituciones")
    public ResponseEntity<?>    getInstituciones(){
        return new ResponseEntity<>(institucionService.getInstituciones(), HttpStatus.FOUND);
    }

    //UPDATE
    @PutMapping("/instituciones/{id}")
    public ResponseEntity<?> upateTarea(@PathVariable Integer id,
                                        @Valid @RequestBody Institucion request){
        institucionService.updateInstitucion(id, request);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Institución actualizada con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    };

    @DeleteMapping("/instituciones/{idInstitucion}")
    public ResponseEntity<?> deleteInstitucion(@Valid @NotNull @PathVariable Integer idInstitucion){
        institucionService.deleteInstitucion(idInstitucion);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Institución eliminada con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    };
}
