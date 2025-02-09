package com.example.shop.exception;

import com.example.shop.dtos.reponse.utils.RestResponse;
import com.example.shop.exception.error.IDException;
import com.example.shop.exception.error.NameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobaleException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        String errorDetail = "Validation failed: " + fieldErrors.size() + " error(s)";
        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        RestResponse<Object> response = RestResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errors.size() > 1 ? errors : errors.get(0))
                .error(errorDetail)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<RestResponse<Object>> exception(RuntimeException e) {
        RestResponse<Object> response = RestResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .error("BAD_REQUEST")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ResponseEntity<RestResponse<Object>> numberFormatException(NumberFormatException e) {
        RestResponse<Object> response = RestResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .error("Input format error: Expected a numeric value.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<RestResponse<Object>> exception(Exception e) {
        RestResponse<Object> response = RestResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .error("Internal Server Error")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = {
            IDException.class,
            NameException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception e) {
        RestResponse<Object> response = RestResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .error("Exception Foccurs...")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
    }
}
