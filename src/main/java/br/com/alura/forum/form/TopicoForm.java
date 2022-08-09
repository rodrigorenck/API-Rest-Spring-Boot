package br.com.alura.forum.form;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repositories.CursoRepository;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

public class TopicoForm {
    //bean validation
    @NotNull @NotEmpty @Size(min = 5)
    private String titulo;
    @NotNull @NotEmpty @Size(min = 10)
    private String mensagem;
    private String nomeCurso;

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public Topico converter(CursoRepository cursoRepository) {
        Optional<Curso> curso = cursoRepository.findByNome(nomeCurso);
        return new Topico(titulo, mensagem, curso.get());
    }

}
