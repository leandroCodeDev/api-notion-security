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

    @GetMapping("")
    public ResponseEntity<List<UsuarioEntity>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getEntities());
    }

    @PostMapping("")
    public ResponseEntity<UsuarioEntity> create(@RequestBody UsuarioEntity entity){
        return ResponseEntity.status(HttpStatus.OK).body(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> getOne(@PathVariable(name = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getEntity(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> update(@PathVariable(name = "id") Long id, @RequestBody UsuarioEntity entity){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id,entity));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
