package com.flosunn.pomodoro.core.cryptography

import kotlinx.serialization.Serializable

@Serializable
data class EncryptedMessage(
    val ciphertext: ByteArray,
    // Extra byte have 2 cases:
    // 1. When the message is encrypted with a symmetric key, this byte contains the IV (initialization vector).
    // 2. When the message is encrypted with an asymmetric key, this byte contains the signature of the message.
    // In both cases, the byte array is used to store additional information needed for decryption
    // or verification of the message.
    val extraBytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedMessage

        if (!ciphertext.contentEquals(other.ciphertext)) return false
        if (!extraBytes.contentEquals(other.extraBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ciphertext.contentHashCode()
        result = 31 * result + extraBytes.contentHashCode()
        return result
    }
}