package com.flosun.pomodoro.core.cryptography

enum class BackendAlgorithm {
    RSA,
    EC
}

fun BackendAlgorithm.toAlgorithmName(): String = when (this) {
    BackendAlgorithm.RSA -> "RSA"
    BackendAlgorithm.EC -> "EC"
}