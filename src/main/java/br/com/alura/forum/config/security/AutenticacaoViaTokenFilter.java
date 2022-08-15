package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Classe para recuperar um token, verificar se esta valido e autenticar ele - fazemos esse processo para cada requisicao do cliente
 * Temos que adicionar esse Filtro na nossa classe SecurityConfigurations
 * Se o usuario chegou nessa parte ele ja tem um token - ele ja autenticou com Usuario e Senha - quem eh responsavel por isso eh o nosso AutenticacaoService
 * Essa classe nao eh um Component do Spring portanto nao conseguimso fazer injecao de dependencias
 */
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository){
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }
    /**
     * Logica para pegar o token do cabecalho - verificar se ele esta ok e caso esteja ok autentica-lo
     * Autenticacao eh feita para cada requisicao - nao existe "usuario esta logado" para Autenticacao Stateless
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //recuperar o token que foi passado no cabecalho - pelo request conseguimos recuperar o que foi passado no cabecalho
        String token = recuperarToken(request);
        //delegamos a verificao do token para a nossa service - que encapsula o uso da API Jwt que sera necessaria para a verificacao
        boolean valido = tokenService.isTokenValido(token);
        //se o token eh valido vamos forcar uma autenticacao do usuario
        if(valido){
            autenticarUsuario(token);
        }

        //essa linha diz: ja rodei o que tinha que rodar nesse filtro, segue a execucao
        filterChain.doFilter(request, response);
    }

    //vamos forcar a autenticacao - autenticacao por usuario e senha ja foi feita na primeira vez que o Usuario se logou - pela AutenticacaoService
    private void autenticarUsuario(String token) {
        //com o token conseguimos recuperar o id do usuario
        Long idUsuario = tokenService.getIdUsuario(token);
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        //passamos como parametro o usuario, a senha (no caso eh null pq ele ja ta logado, ela nao importa), e os perfis de acesso
        UsernamePasswordAuthenticationToken authentication =  new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        //metodo que forca a autenticacao - temos que passar qual o usuario - authentication tem as informacoes do usuario que esta logado
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recuperarToken(HttpServletRequest request) {
        //passamos qual o cabecalho que desejamos recuperar
        String token = request.getHeader("Authorization");
        //temos que verificar se o token ta vindo certinho
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        //retornamos a substring comecando da posicao 7 para que eu retorne o token sem o "Bearer "
        return token.substring(7, token.length());
    }
}
