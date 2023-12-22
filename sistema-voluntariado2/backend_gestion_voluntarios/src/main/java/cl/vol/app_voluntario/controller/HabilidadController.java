package cl.vol.app_voluntario.controller;

import cl.vol.app_voluntario.model.Habilidad;
import cl.vol.app_voluntario.request.UpdateHabilidadRequest;
import cl.vol.app_voluntario.response.ApiResponse;
import cl.vol.app_voluntario.service.HabilidadService;
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
public class HabilidadController {
    private final HabilidadService habilidadService;

    @GetMapping("/habilidades")
    public ResponseEntity<?> updateHabilidad(){
        return new ResponseEntity<>(habilidadService.getHabilidades(), HttpStatus.OK);
    }
    //CREATE HABILIDAD
    @PostMapping("/habilidades")
    public ResponseEntity<?> createHabilidad(
            @Valid @RequestBody Habilidad request,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()) return new ResponseEntity<>(ValidationUtil.getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(habilidadService.createHabilidad(request.getDescripcion()), HttpStatus.CREATED);
    }

    @PutMapping("/habilidades/{idHabilidad}")
    public ResponseEntity<?> updateHabilidad(@Valid @NotNull @PathVariable Integer idHabilidad,
                                          @Valid @RequestBody UpdateHabilidadRequest request){
        habilidadService.updateHabilidad(idHabilidad, request);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Habilidad editada con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    };

    @DeleteMapping("/habilidades/{idHabilidad}")
    public ResponseEntity<?> deleteHabilidad(@Valid @NotNull @PathVariable Integer idHabilidad){
        habilidadService.deleteHabilidad(idHabilidad);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Habilidad eliminada con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    }
}
