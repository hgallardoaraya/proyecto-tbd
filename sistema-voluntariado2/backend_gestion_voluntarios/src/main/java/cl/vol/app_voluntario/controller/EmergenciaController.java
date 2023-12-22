package cl.vol.app_voluntario.controller;

import cl.vol.app_voluntario.request.*;
import cl.vol.app_voluntario.response.ApiResponse;
import cl.vol.app_voluntario.service.EmergenciaService;
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
public class EmergenciaController {
    private final EmergenciaService emergenciaService;
    //CREATE
    @PostMapping("/emergencias")
    public ResponseEntity<?> createEmergencia(
            @Valid @RequestBody CreateEmergenciaRequest request,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()) return new ResponseEntity<>(ValidationUtil.getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        return emergenciaService.createEmergencia(request);
    }

    //READ ALL
    @GetMapping("/emergencias")
    public ResponseEntity<?> getEmergencias(){
        return new ResponseEntity<>(emergenciaService.getEmergencias(), HttpStatus.OK);
    }

    //UPDATE
    @PutMapping("/emergencias/{id}")
    public ResponseEntity<?> updateEmergencia(
            @Valid @NotNull @PathVariable Integer id,
            @Valid @RequestBody UpdateEmergenciaRequest request,
            BindingResult bindingResult){
        if(bindingResult.hasErrors()) return new ResponseEntity<>(ValidationUtil.getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        emergenciaService.updateEmergencia(id, request);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Emergencia editada con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    }

    @DeleteMapping("/emergencias/{idEmergencia}")
    public ResponseEntity<?> deleteEmergencia(@Valid @NotNull @PathVariable Integer idEmergencia){
        emergenciaService.deleteEmergencia(idEmergencia);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Emergencia eliminada con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    }


    //OBTENER VOLUNTARIOS DE UNA EMERGENCIA
    @GetMapping("/emergencias/{id}/voluntarios")
    public ResponseEntity<?> getVoluntariosEmergencia(@PathVariable Integer id) {
        return new ResponseEntity<>(emergenciaService.getVoluntariosEmergencia(id), HttpStatus.OK);
    }

    @GetMapping("/emergencias/{idEmergencia}/habilidades")
    public ResponseEntity<?> getHabilidadesEmergencia(@Valid @NotNull @PathVariable Integer idEmergencia) {
        return new ResponseEntity<>(emergenciaService.getHabilidadesEmergencia(idEmergencia), HttpStatus.OK);
    }

    @PostMapping("/emergencias/{idEmergencia}/habilidades/{idHabilidad}")
    public ResponseEntity<?> addHabilidadEmergencia(
            @Valid @NotNull @PathVariable Integer idEmergencia,
            @Valid @NotNull @PathVariable Integer idHabilidad
    ){
        emergenciaService.addHabilidadEmergencia(idEmergencia, idHabilidad);
        return new ResponseEntity<>("Éxito", HttpStatus.OK);
    }

    @DeleteMapping("/emergencias/{idEmergencia}/habilidades/{idHabilidad}")
    public ResponseEntity<?> deleteHabilidadEmergencia(
            @Valid @NotNull @PathVariable Integer idEmergencia,
            @Valid @NotNull @PathVariable Integer idHabilidad
    ){
        emergenciaService.deleteHabilidadEmergencia(idEmergencia, idHabilidad);
        return new ResponseEntity<>("Éxito", HttpStatus.OK);
    }

    @PutMapping("/emergencias/{idEmergencia}/habilidades/{idHabilidad}")
    public ResponseEntity<?> updateEmeHabilidad(
            @Valid @NotNull @PathVariable Integer idEmergencia,
            @Valid @NotNull @PathVariable Integer idHabilidad,
            @Valid @RequestBody UpdateEmeHabilidadRequest request,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()) return new ResponseEntity<>(ValidationUtil.getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        emergenciaService.updateHabilidadEmergencia(idEmergencia, idHabilidad, request.getIdHabilidad());
        return new ResponseEntity<>("Éxito", HttpStatus.OK);
    }
}
