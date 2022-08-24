package br.com.alura.forum.repositories;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TopicoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private TopicoRepository topicoRepository;

    @Test
    @DisplayName("Devolve um topico pelo nome do curso")
    void devolveUmTopicoPeloNomeDoCurso() {
        //primeiro temos que cadastrar um curso
        Curso curso = new Curso("Spring Boot", "Programacao");
        cursoRepository.save(curso);

        //recuperamos o curso criado
        Curso spring_boot = cursoRepository.findByNome("Spring Boot").get();

        //depois criamos um topico
        Topico topico = new Topico("Duvida relacionamentos", "Nao entendi como faz", spring_boot);
        topicoRepository.save(topico);

        //recuperamos o topico pelo nome do curso
        Stream<Topico> topicosSpring = topicoRepository.findByCursoNome("Spring Boot", PageRequest.of(0, 1)).get();
        Topico topicoCadastrado = topicosSpring.findFirst().get();
        assertEquals(topicoCadastrado, topico);
    }


//    @Test
//    @DisplayName("Devolve um topico pelo nome do curso")
//    void devolveTodosTopicosQuandoNomeCursoForNull() {
//        //primeiro temos que cadastrar um curso
//        Curso curso = new Curso("Spring Boot", "Programacao");
//        Curso curso2 = new Curso("HTML 5", "Programacao");
//        cursoRepository.save(curso);
//        cursoRepository.save(curso2);
//
//        //recuperamos o curso criado
//        Curso spring_boot = cursoRepository.findByNome("Spring Boot").get();
//        Curso html5 = cursoRepository.findByNome("HTML 5").get();
//
//        //depois criamos um topico
//        List<Topico> listTopicosExpected = new ArrayList<>();
//        Topico topico = new Topico("1", "Nao entendi como faz", spring_boot);
//        Topico topico1 = new Topico("2", "Nao entendi como faz", html5);
//        Topico topico2 = new Topico("3", "Nao entendi como faz", html5);
//        Topico topico3 = new Topico("4", "Nao entendi como faz", spring_boot);
//        topicoRepository.save(topico);
//        topicoRepository.save(topico1);
//        topicoRepository.save(topico2);
//        topicoRepository.save(topico3);
//
//        for (Topico t :
//                topicoRepository.findAll()) {
//            listTopicosExpected.add(t);
//        }
//        //recuperamos o topico pelo nome do curso
//        Stream<Topico> topicosSpring = topicoRepository.findByCursoNome(null, PageRequest.of(0, 10)).get();
//        List<Topico> listTopicosActual = topicosSpring.toList();
//        assertEquals(listTopicosExpected, listTopicosActual);
//    }

}