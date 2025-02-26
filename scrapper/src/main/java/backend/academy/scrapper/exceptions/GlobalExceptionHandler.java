package backend.academy.scrapper.exceptions;

import backend.academy.scrapper.dto.ApiErrorResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "Внутренняя ошибка сервера",
                "INTERNAL_ERROR",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                List.of(ex.getStackTrace()).stream()
                        .map(StackTraceElement::toString)
                        .toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
