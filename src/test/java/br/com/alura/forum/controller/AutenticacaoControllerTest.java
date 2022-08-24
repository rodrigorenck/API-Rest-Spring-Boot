package br.com.alura.forum.controller;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AutenticacaoControllerTest {

    //simula o postman
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveriaRetornarUm400ParaDadosInvalidos() throws Exception {
        URI uri = new URI("/auth");
        String json = "{\"email\": \"invalido@email.com\", \"senha\": \"123456\"}";

        mockMvc.perform(
                MockMvcRequestBuilders.post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void deveriaRetornarUm200ParaDadosValidos() throws Exception {
        //populamos o banco
        Usuario usuario = new Usuario("aluno@email.com", "$2a$10$anR7kKWbrxJX5P35RJeAzOwUllwqhhsmXq7MrHf.2THu8yNbFaN9y");
        usuarioRepository.save(usuario);

        URI uri = new URI("/auth");
        String json = "{\"email\": \"aluno@email.com\", \"senha\": \"123456\"}";

        mockMvc.perform(
                        MockMvcRequestBuilders.post(uri)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    //como testar o corpo da requisicao?
//    @Test
//    @DisplayName("Deveria retornar o token e o tipo do token no corpo da requisicao quando dados forem validos")
//    void deveriaRetornarOTokenEOTipoDoTokenNoCorpoDaRequisicaoQuandoDadosForemValidos() throws URISyntaxException {
//        Usuario usuario = new Usuario("aluno@email.com", "$2a$10$anR7kKWbrxJX5P35RJeAzOwUllwqhhsmXq7MrHf.2THu8yNbFaN9y");
//        usuarioRepository.save(usuario);
//
//
//        URI uri = new URI("/auth");
//        String json = "{\"email\": \"aluno@email.com\", \"senha\": \"123456\"}";
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post(uri)
//                                .content(json)
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.content());
//
//
//    }
}