package com.example.jwt.exception;

import com.example.jwt.dtos.response.util.RestResponse;
import com.example.jwt.exception.error.EmailException;
import com.example.jwt.exception.error.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<RestResponse<Object>> exception(Exception e) {
        RestResponse<Object> response = RestResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .error("Internal Server Error")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

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

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<RestResponse<Object>> handleNotFound(ResponseStatusException e) {
        RestResponse<Object> response = RestResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .error("NOT_FOUND")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(response);
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

    @ExceptionHandler(value = {
            EmailException.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception e) {
        RestResponse<Object> response = RestResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .error("Exception Foccurs...")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
    }

    @ExceptionHandler(value = {
            PermissionException.class,
    })
    public ResponseEntity<RestResponse<Object>> handlePermissionException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.FORBIDDEN.value());
        res.setError("Forbidden");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }
}
