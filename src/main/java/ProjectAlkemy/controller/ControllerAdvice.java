package ProjectAlkemy.controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.exception.CommentNotAuthorizatedException;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseDto handleException(Exception e) { return new ResponseDto(0,"Internal server error"); }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    public ResponseDto handleSQLexception() { return new ResponseDto(1, "Internal server error"); }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ListNotFoundException.class)
    public ResponseDto handleListNotFoundException(ListNotFoundException e) { return new ResponseDto(2, e.getMessage()); }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecordNotExistException.class)
    public ResponseDto handleRecordNotExistException(RecordNotExistException e) { return new ResponseDto(3, e.getMessage()); }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CommentNotAuthorizatedException.class)
    public ResponseDto handleNotUpdatedException(CommentNotAuthorizatedException e) {
        return new ResponseDto(4, e.getMessage());
    }


}
