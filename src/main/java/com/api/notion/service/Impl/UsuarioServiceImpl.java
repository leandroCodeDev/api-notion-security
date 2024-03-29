package com.api.notion.service.Impl;

import com.api.notion.entity.UsuarioEntity;
import com.api.notion.exceptions.Error.BadRequestException;
import com.api.notion.repository.UsuarioRepository;
import com.api.notion.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    private final UsuarioRepository repository;

    @Override
    public UsuarioEntity create(UsuarioEntity entity) {
        var antigo = repository.findByLogin(entity.getLogin());
        if(antigo != null){
            throw new BadRequestException("Elemento ja existe");
        }
        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UsuarioEntity update(Long id, UsuarioEntity entity) {
        var usuarioEntity = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));
        if (entity.getLogin() != null &&  !entity.getLogin().isEmpty()){
            usuarioEntity.setLogin(entity.getLogin());
        }

        if (entity.getNome() != null &&  !entity.getNome().isEmpty()){
            usuarioEntity.setNome(entity.getNome());
        }

        if (entity.getSenha() != null &&  !entity.getSenha().isEmpty()){
            usuarioEntity.setSenha(entity.getSenha());
        }
        return repository.save(usuarioEntity);
    }

    @Override
    public UsuarioEntity getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));
    }

    @Override
    public List<UsuarioEntity> getEntities() {
        return repository.findAll();
    }
}
