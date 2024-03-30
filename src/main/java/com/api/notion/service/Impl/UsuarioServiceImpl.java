package com.api.notion.service.Impl;

import com.api.notion.entity.UsuarioEntity;
import com.api.notion.exceptions.Error.BadRequestException;
import com.api.notion.exceptions.Error.ForbiddenException;
import com.api.notion.repository.UsuarioRepository;
import com.api.notion.service.UsuarioService;
import com.api.notion.utils.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder bCryptEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private static long TEMPO_EXPIRACAO = 36000L;


    public UsuarioServiceImpl(UsuarioRepository repository,
                              BCryptPasswordEncoder bCryptEncoder,
                              JwtEncoder jwtEncoder,
                              JwtDecoder jwtDecoder) {
        this.repository = repository;
        this.bCryptEncoder = bCryptEncoder;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }


    @Override
    public void delete(String token,Long id) {
        try{
            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            if(id !=  Long.parseLong(attributes.getSubject())){
                throw new ForbiddenException("Erro na autenticação");
            }

            repository.deleteById(id);

        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }

    }

    @Override
    public UsuarioEntity update(String token,Long id, UsuarioEntity entity) {
        try{
            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            if(id !=  Long.parseLong(attributes.getSubject())){
                throw new ForbiddenException("Erro na autenticação");
            }


            var usuarioEntity = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));
            if (entity.getLogin() != null &&  !entity.getLogin().isEmpty()){
                usuarioEntity.setLogin(entity.getLogin());
            }

            if (entity.getNome() != null &&  !entity.getNome().isEmpty()){
                usuarioEntity.setNome(entity.getNome());
            }

            if (entity.getSenha() != null &&  !entity.getSenha().isEmpty()){
                usuarioEntity.setSenha(bCryptEncoder.encode(entity.getSenha()).toString());
            }



            return repository.saveAndFlush(usuarioEntity);
        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }

    }

    @Override
    public UsuarioEntity getEntity(String token,Long id) {
        try{
            token = TokenUtils.recoverToken(token);
            TokenUtils.TokenAttributes attributes = TokenUtils.parseToken(token);

            UsuarioEntity user = repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));

            if(user.getUsuario_id() !=  Long.parseLong(attributes.getSubject())){
                throw new ForbiddenException("Erro na autenticação");
            }
            return user;
        }catch (ParseException e){
            throw new ForbiddenException("Erro na autenticação");
        }
    }



    @Override
    public UsuarioEntity registrar(UsuarioEntity dto){
        var antigo = repository.findByLogin(dto.getLogin());
        if(antigo != null){
            throw new BadRequestException("Elemento ja existe");
        }
        dto.setSenha(bCryptEncoder.encode(dto.getSenha()).toString());
        return repository.saveAndFlush(dto);
    }

    @Override
    public String login(UsuarioEntity dto){
        var antigo = repository.findByLogin(dto.getLogin());
        if(antigo == null){
            throw new BadRequestException("Elemento não existe");
        }
        if (!TokenUtils.senhaValida(dto.getSenha(), antigo.getSenha())) { //valida se usuario existe e se esta com login correto
            throw new BadRequestException("Erro ao Logar, senha incorreta");
        }


        Instant now = Instant.now(); // momento atual


        // campos do token
        JwtClaimsSet claims = JwtClaimsSet.builder() // campos do JWT
                .issuer("aplicacaodemo") // emissor -> corpo JWT
                .issuedAt(now) // criado no momento atual
                .expiresAt(now.plusSeconds(TEMPO_EXPIRACAO)) // expira em 36000L milisegundos
                .subject(String.valueOf(antigo.getUsuario_id())) //nome do usuário
                .build();

        var valorJWT = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        //Cria um JWT em string com os campos definidos anteriormente
        return valorJWT;

    }



}
