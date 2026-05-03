package com.flosun.pomodoro.core.utils

sealed class DomainException(message: String) : Exception(message)

class NotFoundException(message: String = "Resource not found") : DomainException(message)

class ValidationException(message: String = "Validate Error") : DomainException(message)

class UnauthorizedException(message: String = "Unauthorized access") : DomainException(message)

class DatabaseException(message: String = "Database error occurred") : DomainException(message)
