package com.mxr.integration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import com.mxr.integration.Response.ErrorResponse;
import com.mxr.integration.exceptions.AgifyExceptions.NullAgeException;
import com.mxr.integration.exceptions.NationalizeExceptions.MissingCountryDataException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex){
        ErrorResponse response = new ErrorResponse(
            "error",
            "Required request parameter is missing"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        
    }

    @ExceptionHandler(InvalidNameException.class)
    public ResponseEntity<ErrorResponse> handleInvalidNameException(InvalidNameException ex){
        ErrorResponse response = new ErrorResponse(
            "error",
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MissingOrEmptyNameException.class)
    public ResponseEntity<ErrorResponse> handleMissingOrEmptyNameException(MissingOrEmptyNameException ex){
        ErrorResponse response = new ErrorResponse(
            "error",
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingGenderizeDataException.class)
    public ResponseEntity<ErrorResponse> handleMissingGenderizeDataException(MissingGenderizeDataException ex){
        ErrorResponse response = new ErrorResponse(
            "error",
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException ex){
        ErrorResponse response = new ErrorResponse(
            "error",
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(NullAgeException.class)
    public ResponseEntity<ErrorResponse> handleNullAgeException(NullAgeException ex){
        ErrorResponse response = new ErrorResponse(
            "error",
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(MissingCountryDataException.class)
    public ResponseEntity<ErrorResponse> handleMissingCountryDataException(MissingCountryDataException ex){
        ErrorResponse response = new ErrorResponse(
            "error",
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }   

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        ErrorResponse response = new ErrorResponse(
            "error",
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
