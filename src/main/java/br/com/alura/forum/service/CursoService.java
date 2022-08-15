package br.com.alura.forum.service;

import br.com.alura.forum.form.CursoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository){
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listar(String nome) {
        if(nome == null){
            return cursoRepository.findAll();
        }
        Optional<Curso> optionalCurso = cursoRepository.findByNome(nome);
        return optionalCurso.map(List::of).orElseGet(ArrayList::new);
        //        if(optionalCurso.isPresent()){
//            return List.of(optionalCurso.get());
//        }
//        return new ArrayList<>();
    }

    public Optional<Curso> buscaPorNome(String nome){
        return cursoRepository.findByNome(nome);
    }

    public ResponseEntity<Curso> cadastrar(CursoForm form, UriComponentsBuilder uriBuilder){
        Curso curso = CursoForm.converter(form);
        cursoRepository.save(curso);
        URI uri = uriBuilder.path("cursos/{id}").buildAndExpand(curso.getId()).toUri();
        return ResponseEntity.created(uri).body(curso);
    }
}
