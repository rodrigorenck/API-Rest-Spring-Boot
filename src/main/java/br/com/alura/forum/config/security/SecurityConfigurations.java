package br.com.alura.forum.config.security;

import br.com.alura.forum.repositories.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
//indicamos para qual ambiente eh para carregar essa classe
@Profile(value = {"prod",  "test"})
public class SecurityConfigurations {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final AutenticacaoService autenticacaoService;

    public SecurityConfigurations(TokenService tokenService, UsuarioRepository usuarioRepository, AutenticacaoService autenticacaoService) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.autenticacaoService = autenticacaoService;
    }

    //configuracoes de autenticacao
    //cria o autentication manager que vamos usar na nossa AutenticacaoController
    //AuthenticationManager - autenticacao via usuario e senha
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //configuracoes de autorizacao
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/topicos").permitAll()
                .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                //para executar esse endpoint nao basta estar autenticado, tem que ter o perfil de MODERADOR
                .antMatchers(HttpMethod.DELETE, "/topicos/*").hasRole("MODERADOR")
                //esse endpoint devolve info sensiveis entao quando fizer o deploy da app nao deve estar permitAll
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/**/api-docs").permitAll()
                .antMatchers("**/favicon.ico", "/css/**", "/images/**", "/js/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
                //contra ataque hacker porem ao tornar stateless nao precisamos dessa configuracao
                .and().csrf().disable()
                //acabamos de avisar ao spring que nao é para criar sessions - boa pratica - Autenticacao Stateless
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //adicionamos o nosso filtro (que recupera o token) - antes do filtro padrao do Spring - Faz com que o nosso filtro rode antes de autenticar o usuário
                .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService).passwordEncoder(passwordEncoder());
    }



//    configuracoes de recursos estaticos (js, css, imagens...) -> front end (nao usaremos nesse projeto)
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return (web) -> web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
//    }

//    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("123456"));
//    }


}
