package com.api.notion.service;

import com.api.notion.entity.UsuarioEntity;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    public void delete(String token,Long id);
    public UsuarioEntity update (String token,Long id,UsuarioEntity entity);
    public UsuarioEntity getEntity (String token,Long id);


    UsuarioEntity registrar(UsuarioEntity dto);

    String login(UsuarioEntity dto);
}
