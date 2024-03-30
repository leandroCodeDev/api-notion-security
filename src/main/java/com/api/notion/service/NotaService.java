package com.api.notion.service;

import com.api.notion.entity.NotaEntity;

import java.util.List;

public interface NotaService {

    public NotaEntity create (NotaEntity entity);
    public void delete(Long id);
    public NotaEntity update (Long id,NotaEntity entity);
    public NotaEntity getEntity (Long id);
    public List<NotaEntity> getEntities ();
}
