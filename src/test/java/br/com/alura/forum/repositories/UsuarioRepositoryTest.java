package br.com.alura.forum.repositories;

import br.com.alura.forum.modelo.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Devolve um usuario ao passar o email")
    void devolveUmUsuarioAoPassarOEmail() {
        Usuario usuario = new Usuario("user@gmail.com", "1234");
        usuarioRepository.save(usuario);

        Usuario usuario1 = usuarioRepository.findByEmail("user@gmail.com").get();

        assertEquals("user@gmail.com", usuario1.getEmail());
    }

    @Test
    @DisplayName("Joga uma exception ao buscar usuario com email invalido")
    void jogaUmaExceptionAoBuscarUsuarioComEmailInvalido() {
        Usuario usuario = new Usuario("user@gmail.com", "1234");
        usuarioRepository.save(usuario);

        assertThrows(NoSuchElementException.class, ()->{
            usuarioRepository.findByEmail("usuario@gmail.com").get();
        });
    }
}