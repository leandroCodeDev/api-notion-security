package com.api.notion.service;

import com.api.notion.entity.UsuarioEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    public UsuarioEntity create (UsuarioEntity entity);
    public void delete(Long id);
    public UsuarioEntity update (Long id,UsuarioEntity entity);
    public UsuarioEntity getEntity (Long id);
    public List<UsuarioEntity> getEntities ();

    UsuarioEntity registrar(UsuarioEntity dto);

    String login(UsuarioEntity dto);
}
