package br.com.alura.forum.config.validation;

import br.com.alura.forum.dto.ErroDeFormularioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

    //atributo para conseguirmos personalizar a mensagem de erro para diferentes linguas
    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)//por padrao ele devolve um 200 mas queremos que devolva um 400 - BAD REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class)//metodo sera chamado quando houver uma excecao em alguma controller
    public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception){
        List<ErroDeFormularioDto> dto = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getFieldErrors();
        fieldErrors.forEach(e-> {
            //para descobrir qual o locale do Cliente que esta mandando a request e escrever o erro na lingua dele
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
            dto.add(erro);
        });
        return dto;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handle(NoSuchElementException exception){
        return ResponseEntity.notFound().build();
    }
}
