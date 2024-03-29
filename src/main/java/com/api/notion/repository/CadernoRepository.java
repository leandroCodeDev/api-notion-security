package com.api.notion.repository;

import com.api.notion.entity.CadernoEntity;
import com.api.notion.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CadernoRepository extends JpaRepository<CadernoEntity, Long> {

}
