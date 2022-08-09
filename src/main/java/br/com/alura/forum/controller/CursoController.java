package br.com.alura.forum.controller;

import br.com.alura.forum.form.CursoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.service.CursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService){
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<Curso> listar(String nome){
        return cursoService.listar(nome);
    }

//    @GetMapping
//    public Optional<Curso> buscarPorNome(String nome){
//        return cursoService.buscaPorNome(nome);
//    }

    @PostMapping
    public ResponseEntity<Curso> cadastrar(@RequestBody CursoForm form, UriComponentsBuilder uriBuilder){
        return cursoService.cadastrar(form, uriBuilder);
    }




}
