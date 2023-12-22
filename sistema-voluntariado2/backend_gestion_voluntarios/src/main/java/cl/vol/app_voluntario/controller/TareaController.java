package cl.vol.app_voluntario.controller;

import cl.vol.app_voluntario.request.CreateTareaRequest;
import cl.vol.app_voluntario.request.GetTareasRegionRequest;
import cl.vol.app_voluntario.request.UpdateHabilidadTareaRequest;
import cl.vol.app_voluntario.request.UpdateTareaRequest;
import cl.vol.app_voluntario.response.ApiResponse;
import cl.vol.app_voluntario.service.TareaService;
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
public class TareaController {
    private final TareaService tareaService;

    //READ
    @GetMapping("/tareas")
    public ResponseEntity<?> getTareas(){
        return new ResponseEntity<>(tareaService.getTareas(), HttpStatus.OK);
    };

    //READ ONE
    @GetMapping("/tareas/{id}")
    public ResponseEntity<?> getTarea(@PathVariable Integer id){
        return new ResponseEntity<>(tareaService.getTarea(id), HttpStatus.OK);
    };

    //CREATE
    @PostMapping("/tareas")
    public ResponseEntity<?> createTarea(
            @Valid @RequestBody CreateTareaRequest request,
            BindingResult bindingResult
    ){
        System.out.println(request);
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>
                    (new ApiResponse().builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .messages(ValidationUtil.getValidationErrors(bindingResult))
                            .build(),
                            HttpStatus.BAD_REQUEST);
        };

        System.out.println(request);
        tareaService.createTarea(request);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Tarea creada con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    }

    //TAREA UPDATE
    @PutMapping("/tareas/{id}")
    public ResponseEntity<?> upateTarea(@PathVariable Integer id,
                                        @Valid @RequestBody UpdateTareaRequest request,
                                        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>
                    (new ApiResponse().builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .messages(ValidationUtil.getValidationErrors(bindingResult))
                            .build(),
                            HttpStatus.BAD_REQUEST);
        };

        tareaService.updateTarea(id, request);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Tarea editada con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    };

    @PostMapping("/tareas/region")
    public ResponseEntity<?> getTareasRegion(@Valid @RequestBody GetTareasRegionRequest request){
        return new ResponseEntity<>(tareaService.getTareasRegion(request), HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/tareas/{id}")
    public ResponseEntity<?> deleteTarea(@PathVariable Integer id){
        System.out.println(id);
        tareaService.deleteTarea(id);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Tarea eliminada con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    };

    @GetMapping("/tareas/{idTarea}/habilidades")
    public ResponseEntity<?> getHabilidadesTarea(
            @Valid @NotNull @PathVariable Integer idTarea
    ){

        return new ResponseEntity<>(tareaService.getHabilidadesTarea(idTarea), HttpStatus.OK);
    }

    @PostMapping("/tareas/{idTarea}/habilidades/{idHabilidad}")
    public ResponseEntity<?> createTarea(
            @Valid @NotNull @PathVariable Integer idTarea,
            @Valid @NotNull @PathVariable Integer idHabilidad
    ){
        tareaService.addHabilidadTarea(idTarea, idHabilidad);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Habilidad agregada a la tarea con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    }

    @DeleteMapping("/tareas/{idTarea}/habilidades/{idHabilidad}")
    public ResponseEntity<?> deleteTarea(
            @Valid @NotNull @PathVariable Integer idTarea,
            @Valid @NotNull @PathVariable Integer idHabilidad
    ){
        tareaService.deleteHabilidadTarea(idTarea, idHabilidad);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Habilidad eliminada en la tarea con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    }

    @PutMapping("/tareas/{idTarea}/habilidades/{idHabilidad}")
    public ResponseEntity<?> updateHabilidadTarea(
            @Valid @NotNull @PathVariable Integer idTarea,
            @Valid @NotNull @PathVariable Integer idHabilidad,
            @Valid @RequestBody UpdateHabilidadTareaRequest request,
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

        tareaService.updateHabilidadTarea(idTarea, idHabilidad, request.getIdHabilidad());
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Habilidad actualizada en la tarea con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    }

}
