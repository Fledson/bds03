package com.devsuperior.bds03.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

/***
 * Classe controladora de errors
 */
@ControllerAdvice
public class ResourceExceptionHandler {

    /**
     * Erros de validação são tratados aqui
     * @param e erro
     * @param request
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        // instanciando a classe de personalização do erro
        ValidationError error = new ValidationError();
        // instanciando meu status code
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        // Personalizando meu erro para MethodArgumentNotValidException
        error.setTimeStamp(Instant.now());
        error.setStatus(status.value());
        error.setError("Validation exception");
        error.setMessage("Um ou mais campos não foram preenchidos corretamente, tente novamente");
        error.setPath(request.getRequestURI());


        // percorrendo cada campo que deu erro e alimentando o campo e a mensagem personalizada a minha lista de campos de erro
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {

            /** Adicionando o erro na lista */
            error.addError(fieldError.getField(), fieldError.getDefaultMessage());

        });


        return ResponseEntity.status(status).body(error);
    }

}
