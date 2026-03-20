package com.notification.notificationplatform.common.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicate(ex: DuplicateResourceException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            error = "DUPLICATE_RESOURCE",
            status = 409,
            message = ex.message ?: "Duplicate resource",
            path = request.requestURI
        )
        return ResponseEntity.status(409).body(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        val response = ErrorResponse(
            error = "INVALID_INPUT",
            status = 400,
            message = message,
            path = request.requestURI
        )
        return ResponseEntity.status(400).body(response)
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingHeader(ex: MissingRequestHeaderException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            error = "INVALID_INPUT",
            status = 400,
            message = ex.message ?: "Required header is missing",
            path = request.requestURI
        )
        return ResponseEntity.status(400).body(response)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            error = "RESOURCE_NOT_FOUND",
            status = 404,
            message = ex.message ?: "Resource not found",
            path = request.requestURI
        )
        return ResponseEntity.status(404).body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            error = "INTERNAL_SERVER_ERROR",
            status = 500,
            message = "서버 내부 오류가 발생했습니다",
            path = request.requestURI
        )
        return ResponseEntity.status(500).body(response)
    }
}