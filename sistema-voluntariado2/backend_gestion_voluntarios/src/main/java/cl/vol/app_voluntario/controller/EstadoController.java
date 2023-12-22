package cl.vol.app_voluntario.controller;

import cl.vol.app_voluntario.request.CreateEstadoRequest;
import cl.vol.app_voluntario.request.UpdateEstadoRequest;
import cl.vol.app_voluntario.response.ApiResponse;
import cl.vol.app_voluntario.service.EstadoService;
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
public class EstadoController {
    private final EstadoService estadoService;

    @PostMapping("/estados")
    public ResponseEntity<?> createEstado(
            @Valid @RequestBody CreateEstadoRequest request,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()) return new ResponseEntity<>(ValidationUtil.getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        estadoService.createEstado(request);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Estado creado con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    }

    @GetMapping("/estados")
    public ResponseEntity<?> getEstados(){
        return new ResponseEntity<>(estadoService.getEstados(), HttpStatus.OK);
    };

    //ESTADO UPDATE
    @PutMapping("/estados/{id}")
    public ResponseEntity<?> updateEstado(@PathVariable Integer id,
                                        @Valid @RequestBody UpdateEstadoRequest request,
                                          BindingResult bindingResult){
        if(bindingResult.hasErrors()) return new ResponseEntity<>(ValidationUtil.getValidationErrors(bindingResult), HttpStatus.BAD_REQUEST);
        estadoService.updateEstado(id, request);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Estado editado con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    };

    @DeleteMapping("/estados/{idEstado}")
    public ResponseEntity<?> deleteEstado(@Valid @RequestBody @PathVariable Integer idEstado){
        estadoService.deleteEstado(idEstado);
        Map<String, String> messages = new HashMap<>();
        messages.put("exito", "Estado editado con éxito.");

        return new ResponseEntity<>
                (new ApiResponse().builder()
                        .status(HttpStatus.OK.value())
                        .messages(messages)
                        .build(),
                        HttpStatus.OK);
    };
}
