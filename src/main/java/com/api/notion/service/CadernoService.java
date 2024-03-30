package com.api.notion.service;

import com.api.notion.entity.CadernoEntity;
import com.api.notion.entity.UsuarioEntity;

import java.util.List;

public interface CadernoService {

    public CadernoEntity create (String token,CadernoEntity entity);
    public void delete(String token,Long id);
    public CadernoEntity update (String token,Long id,CadernoEntity entity);
    public CadernoEntity getEntity (String token,Long id);
    public List<CadernoEntity> getEntities (String token);
}
