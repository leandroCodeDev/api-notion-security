package com.api.notion.service.Impl;

import com.api.notion.entity.UsuarioEntity;
import com.api.notion.exceptions.Error.BadRequestException;
import com.api.notion.repository.UsuarioRepository;
import com.api.notion.service.UsuarioService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder bCryptEncoder;
    private final JwtEncoder jwtEncoder;
    private static long TEMPO_EXPIRACAO = 36000L;


    public UsuarioServiceImpl(UsuarioRepository repository,
                              BCryptPasswordEncoder bCryptEncoder,
                              JwtEncoder jwtEncoder) {
        this.repository = repository;
        this.bCryptEncoder = bCryptEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public UsuarioEntity create(UsuarioEntity entity) {
        var antigo = repository.findByLogin(entity.getLogin());
        if(antigo != null){
            throw new BadRequestException("Elemento ja existe");
        }
        entity.setSenha(bCryptEncoder.encode(entity.getSenha()).toString());
        return repository.saveAndFlush(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UsuarioEntity update(Long id, UsuarioEntity entity) {
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
    }

    @Override
    public UsuarioEntity getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new BadRequestException("Elemento não encontrado"));
    }

    @Override
    public List<UsuarioEntity> getEntities() {
        return repository.findAll();
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
        if (!senhaValida(dto.getSenha(), antigo.getSenha())) { //valida se usuario existe e se esta com login correto
            throw new BadRequestException("Erro ao Logar, senha incorreta");
        }


        Instant now = Instant.now(); // momento atual

        //escopo do token
        String scope = "ADMIN";

        // campos do token
        JwtClaimsSet claims = JwtClaimsSet.builder() // campos do JWT
                .issuer("aplicacaodemo") // emissor -> corpo JWT
                .issuedAt(now) // criado no momento atual
                .expiresAt(now.plusSeconds(TEMPO_EXPIRACAO)) // expira em 36000L milisegundos
                .subject(String.valueOf(antigo.getUsuario_id())) //nome do usuário
                .claim("scope", scope) // cria um campo dentro do corpo do JWT
                .build();

        var valorJWT = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        //Cria um JWT em string com os campos definidos anteriormente
        return valorJWT;

    }


    private boolean senhaValida(String usuarioSenha, String requestSenha) {
        return bCryptEncoder.matches(
                usuarioSenha,
                requestSenha
        );
    }
}
