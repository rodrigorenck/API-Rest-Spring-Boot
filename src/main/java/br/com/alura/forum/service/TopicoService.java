package br.com.alura.forum.service;

import br.com.alura.forum.dto.DetalhamentoTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.AtualizaTopicoForm;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repositories.CursoRepository;
import br.com.alura.forum.repositories.TopicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final CursoRepository cursoRepository;

    public TopicoService(TopicoRepository topicoRepository, CursoRepository cursoRepository){
        this.topicoRepository = topicoRepository;
        this.cursoRepository = cursoRepository;
    }

    public Page<TopicoDto> listar(String nomeCurso, Pageable paginacao){
        if(nomeCurso == null) {
            return TopicoDto.converter(topicoRepository.findAll(paginacao));
        }
         return TopicoDto.converter(topicoRepository.findByCursoNome(nomeCurso, paginacao));
    }

    public ResponseEntity<TopicoDto> cadastrar(TopicoForm form, UriComponentsBuilder uriBuilder){
        //temos que converter o nosso topicoForm para um topico
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        //levaremos o Topico que acabamos de criar para a resposta da requisicao
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    public ResponseEntity<DetalhamentoTopicoDto> detalhar(Long id) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        if(topicoOptional.isPresent()){
            return ResponseEntity.ok(new DetalhamentoTopicoDto(topicoOptional.get()));
        }
        //caso nao exista nenhum elemento com esse id devolvemos um 404 - NOT FOUND
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<TopicoDto> atualizar(Long id, AtualizaTopicoForm form) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);
//        if(topicoOptional.isPresent()){
            Topico topico = topicoOptional.get();
            topico.setMensagem(form.getMensagem());
            topico.setTitulo(form.getTitulo());
            return ResponseEntity.ok(new TopicoDto(topico));
        //}
//        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> deletar(Long id) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        if(topicoOptional.isPresent()){
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
