package com.api.notion.controller;


import com.api.notion.entity.UsuarioEntity;
import com.api.notion.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    private final UsuarioService service;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> getOne(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "id") Long id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.getEntity(token,id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> update(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "id") Long id,
            @RequestBody UsuarioEntity entity
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(token,id,entity));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader(name = "Authorization") String token,
                                       @PathVariable(name = "id") Long id){
        service.delete(token,id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
