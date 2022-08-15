package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Classe com as regras para gerar um token
 * Aqui que usaremos a API do JJWT para fazer a geracao do token
 */
@Service
public class TokenService {

    /**
     * Injetamos valores que estao no nosso Application Properties
     */
    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authenticate) {
        //metodo para retornar o usuario - retorna um object e a gente faz o casting
        Usuario usuarioLogado = (Usuario) authenticate.getPrincipal();
        Date hoje = new Date();
        //data do momento que tu criou mais o tempo de vida de um token (tudo em milissegundos)- que no nosso caso colocamos para durar 1 dia
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
        return Jwts.builder()
                //qual aplicacao esta gerando esse token
                .setIssuer("API do Fórum da Alura")
                //quem eh o dono desse Token - a qual usuario ele pertence - passamos o Id do usuario em String
                .setSubject(usuarioLogado.getId().toString())
                //data de geracao do Token - usa a API de datas antiga do Java - Date
                .setIssuedAt(hoje)
                //data de expiracao do Token - tambem usa Date - para ele nao ficar aqui no codigo vamos injetar ele
                .setExpiration(dataExpiracao)
                //gerar a criptografia - passamos o algoritmo de criptografia e a senha da minha aplicacao (secret)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isTokenValido(String token) {
        try {
            Jwts.parser()
                    //passamos a nossa secret para ele descriptografar o token
                    .setSigningKey(secret)
                    //se o token estiver valido ele devolve o token - se nao ele joga uma exception - entao teremos que fazer try catch
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }

    }

    //metodo para recuperar o Id de um usuario pelo token
    public Long getIdUsuario(String token) {
        //getBody devolve as info do token
        Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return Long.parseLong(body.getSubject());
    }
}
