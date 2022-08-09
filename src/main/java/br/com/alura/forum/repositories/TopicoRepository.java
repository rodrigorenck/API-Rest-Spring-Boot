package br.com.alura.forum.repositories;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //nao eh obrigado a por essa anotacao
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    @Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
    List<Topico> buscaTopicosPorCursoNome(String nomeCurso);

    List<Topico> findByCursoNome(String nomeCurso);
}
