package br.com.alura.forum.controller;

import br.com.alura.forum.dto.DetalhamentoTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.AtualizaTopicoForm;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repositories.CursoRepository;
import br.com.alura.forum.repositories.TopicoRepository;
import br.com.alura.forum.service.TopicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService){
        this.topicoService = topicoService;
    }

    @GetMapping//metodos GET passamos o parametro pela URL
    public List<TopicoDto> listar(String nomeCurso) {
        return topicoService.listar(nomeCurso);
    }

    @PostMapping//metodos POST passamos o parametro pela requisicao - @RequestBody
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder){
        return topicoService.cadastrar(form, uriBuilder);
    }

    //passamos o parametro pelo /
    @GetMapping("/{id}")
    public ResponseEntity<DetalhamentoTopicoDto> detalhar(@PathVariable Long id){
        return topicoService.detalhar(id);
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizaTopicoForm form){
        return topicoService.atualizar(id, form);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id){
        return topicoService.deletar(id);
    }
}
