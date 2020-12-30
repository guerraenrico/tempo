package com.enricog.routines.detail.models

internal data class FieldError(
    val field: Field,
    val error: ValidationError
)