package com.fetchrewards.receiptprocessor.controller.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<Map<String, String>> {
        val status = resolveAnnotatedResponseStatus(e)
        return ResponseEntity.status(status).body(
            mapOf(
                "error" to e.javaClass.simpleName,
                "message" to (e.message ?: "Unknown error"),
                "stack_trace" to e.stackTraceToString()
            )
        )
    }
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors = HashMap<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            errors[fieldName] = errorMessage.orEmpty()
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<Map<String, String>> {
        val rootCause = getRootCause(e)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            mapOf(
                "error" to "Invalid request format",
                "message" to (rootCause.message ?: "Unknown error")
            )
        )
    }

    private fun getRootCause(e: Throwable): Throwable {
        var cause: Throwable? = e
        while (cause?.cause != null && cause.cause != cause) {
            cause = cause.cause
        }
        return cause ?: e
    }

    private fun resolveAnnotatedResponseStatus(e: Exception): HttpStatus {
        return e::class.java.getAnnotation(ResponseStatus::class.java)?.value ?: HttpStatus.INTERNAL_SERVER_ERROR
    }
}