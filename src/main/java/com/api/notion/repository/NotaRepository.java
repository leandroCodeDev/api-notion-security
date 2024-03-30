package com.api.notion.repository;

import com.api.notion.entity.CadernoEntity;
import com.api.notion.entity.NotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<NotaEntity, Long> {

}
