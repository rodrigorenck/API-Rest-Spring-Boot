package br.com.alura.forum.repositories;

import br.com.alura.forum.modelo.Curso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//controle transacional automatico
@DataJpaTest
//dizemos para o Spring que nao queremos que ele substitua o Banco que estamos usando pelo Banco em memoria (h2) -> nao substitua minhas configuracoes
//se voce nao colocar essa anotacao ele vai substituir suas configuracoes e utilizaro banco de dados em memoria
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//outra maneira de escolher o ambiente
//forca o profile ativo ser "test" -> dai ele vai ler o application-test.properties
@ActiveProfiles("test")
class CursoRepositoryTest {

    @Autowired
    private CursoRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void deveriaCarregarUmCursoAoPassarSeuNome() {
        String nomeCurso = "HTML 5";

        //como nosso banco no ambiente de teste esta sempre vazio, temos que criar o cenario de cada teste
        Curso html5 = new Curso(nomeCurso);
        html5.setCategoria("Programacao");
        em.persist(html5);

        Optional<Curso> cursoOptional = repository.findByNome(nomeCurso);
        Curso curso = cursoOptional.get();
        assertEquals(nomeCurso, curso.getNome());
    }

    @Test
    void deveriaJogarUmaExceptionParaCursoCujoNomeNaoFoiCadastrado(){
        String nomeCurso = "JPA 3000";
        Optional<Curso> cursoOptional = repository.findByNome(nomeCurso);

        assertThrows(NoSuchElementException.class, ()->{
            cursoOptional.get();
        });
    }

}