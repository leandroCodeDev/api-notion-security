package com.api.notion.service;

import com.api.notion.entity.CadernoEntity;
import com.api.notion.entity.UsuarioEntity;

import java.util.List;

public interface CadernoService {

    public CadernoEntity create (CadernoEntity entity);
    public void delete(Long id);
    public CadernoEntity update (Long id,CadernoEntity entity);
    public CadernoEntity getEntity (Long id);
    public List<CadernoEntity> getEntities ();
}
