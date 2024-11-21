package com.tempo.assessment.rolesapp.exception

import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
//import java.lang.Exception
import kotlin.Exception


@ControllerAdvice
class GlobalExceptionHandler: ResponseEntityExceptionHandler() {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

//  This is overloading the project exceptions. :(
//    @ExceptionHandler(Exception::class)
//    fun handleException(
//        ex: Exception,
//        webRequest: ServletWebRequest
//    ): ResponseEntity<ErrorResponse> {
//        val errorResponse = ErrorResponse(ex.message)
//        log.error("Unhandled exception", ex)
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
//    }

    @ExceptionHandler(RequiredResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: RequiredResourceNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(ex.message)
        log.error("RequiredResourceNotFoundException", ex)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(ResourceAlreadyExistsException::class)
    fun handleResourceNotFoundException(ex: ResourceAlreadyExistsException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(ex.message)
        log.error("ResourceAlreadyExistsException", ex)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        webRequest: ServletWebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse("Invalid input", ex.constraintViolations.map { it.messageTemplate })
        log.error("Constraint violation", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    data class ErrorResponse(val message: String?, val errors: List<String?>? = null)
}