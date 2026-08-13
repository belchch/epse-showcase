package dev.epse.app.aspect.exceptions

import com.fasterxml.jackson.core.JsonProcessingException
import jakarta.persistence.EntityNotFoundException
import org.hibernate.TypeMismatchException
import org.hibernate.exception.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(EntityNotFoundException::class, NoSuchElementException::class)
    fun handleNotFound(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message ?: "Not found"))
    }

    @ExceptionHandler(
        IllegalArgumentException::class,
        JsonProcessingException::class,
        MethodArgumentNotValidException::class,
        ConstraintViolationException::class,
        HttpMessageNotReadableException::class,
        MissingServletRequestParameterException::class,
        TypeMismatchException::class,
        HttpRequestMethodNotSupportedException::class
    )
    fun handleBadRequest(e: Exception): ResponseEntity<ErrorResponse> {
        log.warn("Bad request: {}", e.message)
        return ResponseEntity.badRequest().body(ErrorResponse(e.message ?: "Bad request"))
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleUnauthorized(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponse(e.message ?: "Unauthorized")
        )
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAccessDenied(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ErrorResponse(e.message ?: "Forbidden")
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("Unhandled exception", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse("Internal server error")
        )
    }
}
