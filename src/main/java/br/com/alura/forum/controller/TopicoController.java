package br.com.alura.forum.controller;

import br.com.alura.forum.dto.DetalhamentoTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.AtualizaTopicoForm;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.service.TopicoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService){
        this.topicoService = topicoService;
    }

    @GetMapping//RequestParam - parametros passados pela URL - por padrao sao required!
    //no value eh um identificador desse cache - id do cache
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDto> listar(@RequestParam(required = false) String nomeCurso,
                                  @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable paginacao) {
        return topicoService.listar(nomeCurso, paginacao);
    }

    @PostMapping//metodos POST passamos o parametro pela requisicao - @RequestBody
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
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
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizaTopicoForm form){
        return topicoService.atualizar(id, form);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<?> deletar(@PathVariable Long id){
        return topicoService.deletar(id);
    }
}
