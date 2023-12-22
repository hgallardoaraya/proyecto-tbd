package cl.vol.app_voluntario.controller;

import cl.vol.app_voluntario.request.CreateVolTareaRequest;
import cl.vol.app_voluntario.request.UpdateHabilidadVoluntarioRequest;
import cl.vol.app_voluntario.request.UpdateTareaRequest;
import cl.vol.app_voluntario.request.UpdateTareaVoluntarioRequest;
import cl.vol.app_voluntario.service.VoluntarioService;
import cl.vol.app_voluntario.util.ValidationUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VoluntarioController {
    private final VoluntarioService voluntarioService;

    @GetMapping("/voluntarios")
    public ResponseEntity<?> getVoluntarios(){
        return new ResponseEntity<>(voluntarioService.getVoluntarios(), HttpStatus.OK);
    }

    @GetMapping("/voluntarios/{idUsuario}/habilidades")
    public ResponseEntity<?> getHabilidadesVoluntario(
            @Valid @NotNull @PathVariable Integer idUsuario
    ){
        return new ResponseEntity<>(voluntarioService.getHabilidadesVoluntario(idUsuario), HttpStatus.OK);
    }

    @PostMapping("/voluntarios/{idUsuario}/habilidades/{idHabilidad}")
    public ResponseEntity<?> addHabilidadVoluntario(
            @Valid @NotNull @PathVariable Integer idUsuario,
            @Valid @NotNull @PathVariable Integer idHabilidad
    ){
        voluntarioService.addHabilidadVoluntario(idUsuario, idHabilidad);
        return new ResponseEntity<>("Éxito", HttpStatus.OK);
    }

    @DeleteMapping("/voluntarios/{idUsuario}/habilidades/{idHabilidad}")
    public ResponseEntity<?> deleteHabilidadVoluntario(
            @Valid @NotNull @PathVariable Integer idUsuario,
            @Valid @NotNull @PathVariable Integer idHabilidad
    ){
        voluntarioService.deleteHabilidadVoluntario(idUsuario, idHabilidad);
        return new ResponseEntity<>("Éxito", HttpStatus.OK);
    }

    @PutMapping("/voluntarios/{idUsuario}/habilidades/{idHabilidad}")
    public ResponseEntity<?> updateHabilidadVoluntario(
            @Valid @NotNull @PathVariable Integer idUsuario,
            @Valid @NotNull @PathVariable Integer idHabilidad,
            @Valid @RequestBody UpdateHabilidadVoluntarioRequest request,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()) return new ResponseEntity<>(ValidationUtil.getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        voluntarioService.updateHabilidadVoluntario(idUsuario, idHabilidad, request.getIdHabilidad());
        return new ResponseEntity<>("Éxito", HttpStatus.OK);
    }

    @GetMapping("/voluntarios/{idUsuario}/tareas")
    public ResponseEntity<?> getTareasVoluntario(
            @Valid @NotNull @PathVariable Integer idUsuario
    ){
        return new ResponseEntity<>(voluntarioService.getTareasVoluntario(idUsuario), HttpStatus.OK);
    }

    @PostMapping("/voluntarios/{idUsuario}/tareas/{idTarea}")
    public ResponseEntity<?> addTareaVoluntario(
            @Valid @NotNull @PathVariable Integer idUsuario,
            @Valid @NotNull @PathVariable Integer idTarea,
            @Valid @RequestBody CreateVolTareaRequest request,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()) return new ResponseEntity<>(ValidationUtil.getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        voluntarioService.addTareaVoluntario(idUsuario, idTarea, request);
        return new ResponseEntity<>("Exito", HttpStatus.OK);
    }

    @PutMapping("/voluntarios/{idUsuario}/tareas/{idTarea}")
    public ResponseEntity<?> updateTareaVoluntario(
            @Valid @NotNull @PathVariable Integer idUsuario,
            @Valid @NotNull @PathVariable Integer idTarea,
            @Valid @RequestBody UpdateTareaVoluntarioRequest request,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()) return new ResponseEntity<>(ValidationUtil.getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        voluntarioService.updateTareaVoluntario(idUsuario, idTarea, request.getIdTarea());
        return new ResponseEntity<>("Exito", HttpStatus.OK);
    }

    @DeleteMapping("/voluntarios/{idUsuario}/tareas/{idTarea}")
    public ResponseEntity<?> updateTareaVoluntario(
            @Valid @NotNull @PathVariable Integer idUsuario,
            @Valid @NotNull @PathVariable Integer idTarea
    ){
        voluntarioService.deleteTareaVoluntario(idUsuario, idTarea);
        return new ResponseEntity<>("Exito", HttpStatus.OK);
    }
}
