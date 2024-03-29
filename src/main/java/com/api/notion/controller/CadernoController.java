package com.api.notion.controller;


import com.api.notion.entity.CadernoEntity;
import com.api.notion.service.CadernoService;
import com.api.notion.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cadernos")
public class CadernoController {

    public CadernoController(CadernoService service) {
        this.service = service;
    }

    private final CadernoService service;

    @GetMapping("")
    public ResponseEntity<List<CadernoEntity>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getEntities());
    }

    @PostMapping("")
    public ResponseEntity<CadernoEntity> create(@RequestBody CadernoEntity entity){
        return ResponseEntity.status(HttpStatus.OK).body(service.create(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CadernoEntity> getOne(@PathVariable(name = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getEntity(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CadernoEntity> update(@PathVariable(name = "id") Long id, @RequestBody CadernoEntity entity){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id,entity));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
