package br.com.alura.forum.repositories;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //nao eh obrigado a por essa anotacao
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    //nunca usa @Query!-> so casos extremos
//    @Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
//    List<Topico> buscaTopicosPorCursoNome(String nomeCurso);

    Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);
}
