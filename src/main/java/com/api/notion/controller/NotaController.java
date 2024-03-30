package com.api.notion.controller;


import com.api.notion.entity.NotaEntity;
import com.api.notion.service.NotaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notas")
public class NotaController {

    public NotaController(NotaService service) {
        this.service = service;
    }

    private final NotaService service;

    @GetMapping("")
    public ResponseEntity<List<NotaEntity>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getEntities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaEntity> getOne(@PathVariable(name = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getEntity(id));
    }

    @PostMapping("")
    public ResponseEntity<NotaEntity> create(@RequestBody NotaEntity entity){
        return ResponseEntity.status(HttpStatus.OK).body(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaEntity> update(@PathVariable(name = "id") Long id, @RequestBody NotaEntity entity){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id,entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
