package com.api.notion.service.Impl;

import com.api.notion.entity.NotaEntity;
import com.api.notion.exceptions.Error.BadRequestException;
import com.api.notion.repository.CadernoRepository;
import com.api.notion.repository.NotaRepository;
import com.api.notion.repository.UsuarioRepository;
import com.api.notion.service.NotaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotaServiceImpl implements NotaService {

    private final NotaRepository repository;
    private final CadernoRepository cadernoRepository;
    private final UsuarioRepository usuarioRepository;
    public NotaServiceImpl(NotaRepository repository,CadernoRepository cadernoRepository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.cadernoRepository = cadernoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    

    @Override
    public NotaEntity create(NotaEntity entity) {
        usuarioRepository.findById(entity.getUsuario().getUsuario_id()).orElseThrow(() -> new BadRequestException("Elemento associado não existe"));
        cadernoRepository.findById(entity.getCaderno().getCaderno_id()).orElseThrow(() -> new BadRequestException("Elemento associado não existe"));
        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public NotaEntity update(Long id, NotaEntity entity) {
        var notaEntity = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));

        if (entity.getTitulo() != null &&  !entity.getTitulo().isEmpty()){
            notaEntity.setTitulo(entity.getTitulo());
        }

        if (entity.getConteudo() != null &&  !entity.getConteudo().isEmpty()){
            notaEntity.setConteudo(entity.getConteudo());
        }
        return repository.save(notaEntity);
    }

    @Override
    public NotaEntity getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));
    }

    @Override
    public List<NotaEntity> getEntities() {
        return repository.findAll();
    }
}
