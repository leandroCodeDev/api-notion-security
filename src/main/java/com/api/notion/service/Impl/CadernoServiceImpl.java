package com.api.notion.service.Impl;

import com.api.notion.entity.CadernoEntity;
import com.api.notion.entity.CadernoEntity;
import com.api.notion.exceptions.Error.BadRequestException;
import com.api.notion.exceptions.Error.ForbiddenException;
import com.api.notion.repository.CadernoRepository;
import com.api.notion.repository.UsuarioRepository;
import com.api.notion.service.CadernoService;
import com.api.notion.service.UsuarioService;
import com.api.notion.utils.TokenUtils;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class CadernoServiceImpl implements CadernoService {

    private final CadernoRepository repository;
    private final UsuarioRepository usuarioRepository;

    private final JwtDecoder jwtDecoder;
    public CadernoServiceImpl(CadernoRepository repository,UsuarioRepository usuarioRepository,JwtDecoder jwtDecoder) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.jwtDecoder = jwtDecoder;
    }

    

    @Override
    public CadernoEntity create(String token,CadernoEntity entity) {
        usuarioRepository.findById(entity.getUsuario().getUsuario_id()).orElseThrow(() -> new BadRequestException("Elemento associado não existe"));
        return repository.save(entity);
    }

    @Override
    public void delete(String token,Long id) {

        try{
            var cadernoEntity = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));

            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            if(cadernoEntity.getUsuario().getUsuario_id() !=  Long.parseLong(attributes.getSubject())){
                throw new ForbiddenException("Erro na autenticação");
            }


            repository.deleteById(id);

        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }

    }

    @Override
    public CadernoEntity update(String token,Long id, CadernoEntity entity) {

        try{
            var cadernoEntity = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));

            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            if(cadernoEntity.getUsuario().getUsuario_id() !=  Long.parseLong(attributes.getSubject())){
                throw new ForbiddenException("Erro na autenticação");
            }

            if (entity.getNome() != null &&  !entity.getNome().isEmpty()){
                cadernoEntity.setNome(entity.getNome());
            }

            return repository.save(cadernoEntity);

        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }



    }

    @Override
    public CadernoEntity getEntity(String token,Long id) {
        try{
            var entity = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));

            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            if(entity.getUsuario().getUsuario_id() !=  Long.parseLong(attributes.getSubject())){
                throw new ForbiddenException("Erro na autenticação");
            }

            return  entity;

        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }

    }

    @Override
    public List<CadernoEntity> getEntities(String token) {
        return repository.findAll();
    }



}
