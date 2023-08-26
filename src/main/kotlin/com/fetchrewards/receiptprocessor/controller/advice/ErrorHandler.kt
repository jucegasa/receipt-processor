package com.fetchrewards.receiptprocessor.controller.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
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

    private fun resolveAnnotatedResponseStatus(e: Exception): HttpStatus {
        return e::class.java.getAnnotation(ResponseStatus::class.java)?.value ?: HttpStatus.INTERNAL_SERVER_ERROR
    }
}