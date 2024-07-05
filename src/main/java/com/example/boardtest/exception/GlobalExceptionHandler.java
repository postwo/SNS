package com.example.boardtest.exception;

import com.example.boardtest.model.error.ClientErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<ClientErrorResponse> handleClientErrorException(ClientErrorException e){
        return new ResponseEntity<>(
          new ClientErrorResponse(e.getStatus(),e.getMessage()),e.getStatus());
    }

    //validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ClientErrorResponse> handleClientErrorException(MethodArgumentNotValidException e){

        //클라이언트가 알아야 할 정보만
        var errorMessage = e.getFieldErrors().stream().map(fieldError -> (fieldError.getField()+": "+fieldError.getDefaultMessage()))
                .toList()
                .toString();

        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.BAD_REQUEST,errorMessage),HttpStatus.BAD_REQUEST);
    }

    //validation (none에서 요청했을때)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ClientErrorResponse> handleClientErrorException(HttpMessageNotReadableException e){
        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.BAD_REQUEST,e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    
    
    //server에러 는 클라이언트에게 알려주면안된다 보안적인 이슈가 발생 ==500에러 발생
    //server에러

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ClientErrorResponse> handleruntimeException(RuntimeException e){
        return ResponseEntity.internalServerError().build();
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ClientErrorResponse> handleException(Exception e){
        return ResponseEntity.internalServerError().build();
    }

}
