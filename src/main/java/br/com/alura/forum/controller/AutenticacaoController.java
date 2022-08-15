package br.com.alura.forum.controller;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.dto.TokenDto;
import br.com.alura.forum.form.LoginForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService){
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    //metodo que vai ser chamado pelo Cliente (front end) quando o usuario passar os dados de login e senha
    //no corpo da requisicao o Cliente (front end) vai passar o email e a senha que foram passados pelo usuario
    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form){
        UsernamePasswordAuthenticationToken dadosLogin = form.converterParaAuthenticationToken();

        try{
            //quando cair nessa linha o Spring vai chamar a nossa AutenticacaoService
            Authentication authenticate = authenticationManager.authenticate(dadosLogin);
            //antes de devolver o ok devemos gerar um token
            //recebemos um authenticate para extrair o usuario dele - e entao atribuir o token a esse usuario
            String token = tokenService.gerarToken(authenticate);
            //devolvemos o Token para o cliente em forma de DTO - depois disso o cliente eh responsavel por guardar esse token em algum lugar - memoria, cookie, ...
            //alem do token passamos tambem o tipo de autenticacao que no caso eh Bearer (?)
            return ResponseEntity.ok(new TokenDto(token, "Bearer"));
        }catch (AuthenticationException exception){
            //se o usuario ou senha estiverem errados vai dar uma exception e eu devolvo um Bad Request - erro 400
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}
