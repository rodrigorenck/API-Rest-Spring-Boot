package br.com.alura.forum.repositories;

import br.com.alura.forum.modelo.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByNome(String nome);
}
