package com.api.notion.repository;

import com.api.notion.entity.CadernoEntity;
import com.api.notion.entity.NotaEntity;
import com.api.notion.entity.UsuarioEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CadernoRepository extends JpaRepository<CadernoEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "select * from caderno where  usuario_id  = :usuarioId", nativeQuery = true)
    List<CadernoEntity> findAllByUsuario(Long usuarioId);

}
