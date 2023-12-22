package cl.vol.app_voluntario.controller;

import cl.vol.app_voluntario.service.CoordinadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CoordinadorController {
    private final CoordinadorService coordinadorService;
    @GetMapping("/coordinadores")
    public ResponseEntity<?> getCoordinadores(){
        return new ResponseEntity<>(coordinadorService.getCoordinadores(), HttpStatus.OK);
    }
}
