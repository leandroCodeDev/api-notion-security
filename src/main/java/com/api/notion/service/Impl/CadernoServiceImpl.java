package com.api.notion.service.Impl;

import com.api.notion.entity.CadernoEntity;
import com.api.notion.entity.CadernoEntity;
import com.api.notion.exceptions.Error.BadRequestException;
import com.api.notion.repository.CadernoRepository;
import com.api.notion.repository.UsuarioRepository;
import com.api.notion.service.CadernoService;
import com.api.notion.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadernoServiceImpl implements CadernoService {

    private final CadernoRepository repository;
    private final UsuarioRepository usuarioRepository;
    public CadernoServiceImpl(CadernoRepository repository,UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    

    @Override
    public CadernoEntity create(CadernoEntity entity) {
        usuarioRepository.findById(entity.getUsuario().getUsuario_id()).orElseThrow(() -> new BadRequestException("Elemento associado não existe"));
        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public CadernoEntity update(Long id, CadernoEntity entity) {
        var cadernoEntity = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));

        if (entity.getNome() != null &&  !entity.getNome().isEmpty()){
            cadernoEntity.setNome(entity.getNome());
        }

        return repository.save(cadernoEntity);
    }

    @Override
    public CadernoEntity getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));
    }

    @Override
    public List<CadernoEntity> getEntities() {
        return repository.findAll();
    }
}
