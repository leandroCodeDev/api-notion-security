package com.api.notion.service.Impl;

import com.api.notion.entity.NotaEntity;
import com.api.notion.exceptions.Error.BadRequestException;
import com.api.notion.exceptions.Error.ForbiddenException;
import com.api.notion.repository.CadernoRepository;
import com.api.notion.repository.NotaRepository;
import com.api.notion.repository.UsuarioRepository;
import com.api.notion.service.NotaService;
import com.api.notion.utils.TokenUtils;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class NotaServiceImpl implements NotaService {

    private final NotaRepository repository;
    private final CadernoRepository cadernoRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtDecoder jwtDecoder;


    public NotaServiceImpl(NotaRepository repository,CadernoRepository cadernoRepository, UsuarioRepository usuarioRepository,JwtDecoder jwtDecoder) {
        this.repository = repository;
        this.cadernoRepository = cadernoRepository;
        this.usuarioRepository = usuarioRepository;
        this.jwtDecoder = jwtDecoder;
    }

    

    @Override
    public NotaEntity create(String token,NotaEntity entity) {
        usuarioRepository.findById(entity.getUsuario().getUsuario_id()).orElseThrow(() -> new BadRequestException("Elemento associado não existe"));
        cadernoRepository.findById(entity.getCaderno().getCaderno_id()).orElseThrow(() -> new BadRequestException("Elemento associado não existe"));
        return repository.save(entity);
    }

    @Override
    public void delete(String token,Long id) {
        try{
            var notaEntity = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));

            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            if(notaEntity.getUsuario().getUsuario_id() !=  Long.parseLong(attributes.getSubject())){
                throw new ForbiddenException("Erro na autenticação");
            }

            repository.deleteById(id);

        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }

    }

    @Override
    public NotaEntity update(String token,Long id, NotaEntity entity) {

        try{
            var notaEntity = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));

            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            if(notaEntity.getUsuario().getUsuario_id() !=  Long.parseLong(attributes.getSubject())){
                throw new ForbiddenException("Erro na autenticação");
            }


            if (entity.getTitulo() != null &&  !entity.getTitulo().isEmpty()){
                notaEntity.setTitulo(entity.getTitulo());
            }

            if (entity.getConteudo() != null &&  !entity.getConteudo().isEmpty()){
                notaEntity.setConteudo(entity.getConteudo());
            }
            return repository.save(notaEntity);

        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }

    }

    @Override
    public NotaEntity getEntity(String token,Long id) {
        try{
            var nota = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));

            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            if(nota.getUsuario().getUsuario_id() !=  Long.parseLong(attributes.getSubject())){
                throw new ForbiddenException("Erro na autenticação");
            }


            return nota;
        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }

    }

    @Override
    public List<NotaEntity> getEntities(String token) {
        try{
            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            if(attributes.getSubject() == null || attributes.getSubject().isEmpty()){
                throw new ForbiddenException("Erro na autenticação");
            }

            return repository.findAllByUsuario(Long.parseLong(attributes.getSubject()));

        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }

    }

    @Override
    public List<NotaEntity> getEntities(String token,Long idCaderno) {
        try{
            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            if(attributes.getSubject() == null || attributes.getSubject().isEmpty()){
                throw new ForbiddenException("Erro na autenticação");
            }

            return repository.findAllByUsuarioAndCaderno(Long.parseLong(attributes.getSubject()),idCaderno);

        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }

    }

}
