package br.com.alura.forum.form;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CursoForm {

    @Autowired
    private CursoRepository cursoRepository;
    private String nome;
    private String categoria;

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public static Curso converter(CursoForm form){
        return new Curso(form.nome, form.categoria);
    }

}
