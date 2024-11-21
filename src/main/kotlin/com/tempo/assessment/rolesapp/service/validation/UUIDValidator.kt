package com.tempo.assessment.rolesapp.service.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class UUIDValidator : ConstraintValidator<ValidUUID, String> {

    override fun initialize(constraintAnnotation: ValidUUID) {
        // Nenhuma inicialização necessária
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value == null || isValidUUID(value)
    }

    private fun isValidUUID(value: String): Boolean {
        return try {
            java.util.UUID.fromString(value)
            true
        } catch (ex: IllegalArgumentException) {
            false
        }
    }
}