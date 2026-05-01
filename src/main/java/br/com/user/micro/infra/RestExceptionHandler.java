package br.com.user.micro.infra;

import br.com.user.micro.exceptions.*;
import com.mongodb.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private Map<String, String> mapError(FieldError fieldError) {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("field", fieldError.getField());
        mapping.put("message", fieldError.getDefaultMessage());

        return mapping;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            org.springframework.http.HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Inconsistent request fields");

        List<Map<String, String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapError)
                .toList();
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    private ResponseEntity<DefaultErrorResponse> responseConstructor(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new DefaultErrorResponse(status, message));
    }

    @ExceptionHandler(PermissionDeniedException.class)
    private ResponseEntity<DefaultErrorResponse> permissionDeniedHandler(RuntimeException exception) {
        return this.responseConstructor(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage()
        );
    }

    @ExceptionHandler({
            UserAlreadyRegisteredException.class,
            DifferentPasswordsException.class
    })
    private ResponseEntity<DefaultErrorResponse> conflictHandler(RuntimeException exception) {
        return this.responseConstructor(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            RoleNotFoundException.class,
            SessionNotFoundException.class
    })
    private ResponseEntity<DefaultErrorResponse> notFoundHandler(RuntimeException exception) {
        return this.responseConstructor(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
    }

    @ExceptionHandler(ErrorExecutingOperationException.class)
    private ResponseEntity<DefaultErrorResponse> applicationErrorHandler(RuntimeException exception) {
        return this.responseConstructor(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
    }

//    ----  System errors  ----

    @ExceptionHandler(DuplicateKeyException.class)
    private ResponseEntity<DefaultErrorResponse> systemDuplicityOfDataHandler(RuntimeException exception) {
        return this.responseConstructor(
                HttpStatus.CONFLICT,
                "This record has already been registered!"
        );
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<DefaultErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException exception) {
        return responseConstructor(
                HttpStatus.FORBIDDEN,
                "You do not have permission to access this resource!"
        );
    }

    @ExceptionHandler({
            MongoException.class,
            DataAccessException.class
    })
    private ResponseEntity<DefaultErrorResponse> systemDatabaseAccessErrorHandler(RuntimeException exception) {
        return this.responseConstructor(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Error connecting to the database!"
        );
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<DefaultErrorResponse> unmappedErrorsHandler(Exception exception) {
        return this.responseConstructor(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected internal error!"
        );
    }
}
