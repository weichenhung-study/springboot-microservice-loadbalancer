package com.ntou.exceptions;

import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import com.ntou.spec.SvcRes;
import com.ntou.tool.Common;
import com.ntou.tool.ResTool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@ControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {
    @AllArgsConstructor
    public enum RC {
        SVE9("SVE9", "Throwable")
        , SVE8("SVE8", "UnrecognizedPropertyException")
        , SVE7("SVE7", "NullPointerException")
        , SVE6("SVE6", "NotFoundException")
        , SVE5("SVE5", "NotAllowedException")
        , SVE4("SVE4", "JsonParseException")
        , SVE3("SVE3", "Exception")
        , SVE2("SVE2", "DateTimeParseException")

        , SVEA("SVEA", "JsonMappingException")
        , SVEB("SVEB", "IOException")
        , SVEC("SVEC", "JsonProcessingException")
        , SVED("SVED", "ValidationException")
        , SVEE("SVEE", "WebApplicationException")
        , SVEG("SVEG", "APINotFundException")
        , SVEH("SVEH", "HttpRequestMethodNotSupportedException")
        ;
        private final String code;
        @Getter
        private final String content;
        @JsonValue
        public String getCode() {return code;}
    }
    private static ResponseEntity<?> genResponse(TException e){
        log.warn(Common.EXCEPTION,e);

        SvcRes output = new SvcRes();
        ResTool.setRes(output, e.res.getResCode(), e.res.getResMsg());

        log.info(Common.RES + output);
        log.info(Common.API_DIVIDER + Common.END_B + Common.API_DIVIDER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(output);
    }
    private static ResponseEntity<?> genResponse(RC rc, final Throwable e, HttpStatus status) {
        log.error(Common.EXCEPTION,e);

        SvcRes output = new SvcRes();
        ResTool.setRes(output, rc.getCode(), rc.getContent());

        log.info(Common.RES + output);
        log.info(Common.API_DIVIDER + Common.END_B + Common.API_DIVIDER);
        return ResponseEntity.status(status).body(output);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return genResponse(RC.SVE3, e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(TException.class)
    public ResponseEntity<?> handleException(TException e) {
        return genResponse(e);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException e) {
        return genResponse(RC.SVE7, e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleThrowable(Throwable e) {
        return genResponse(RC.SVE9, e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<?> handleUnrecognizedPropertyException(UnrecognizedPropertyException e) {
        return genResponse(RC.SVE8, e, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<?> handleNotAllowedException(MethodNotAllowedException e) {
        return genResponse(RC.SVE5, e, HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<?> handleJsonParseException(JsonParseException e) {
        return genResponse(RC.SVE4, e, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<?> handleJsonMappingException(JsonMappingException e) {
        return genResponse(RC.SVEA, e, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException e) {
        return genResponse(RC.SVE2, e, HttpStatus.BAD_REQUEST);
    }
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException e,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(Common.EXCEPTION,e);
        SvcRes output = new SvcRes();
        ResTool.setRes(output, RC.SVEH.getCode(), RC.SVEH.getContent());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(output);
    }
}
