package com.flosun.pomodoro.core.cryptography

import android.security.keystore.KeyProperties
import com.flosun.pomodoro.BuildConfig
import javax.crypto.Cipher

interface Cryptography {
    fun initializeCipherEncryption(): Cipher
    fun encryptWithCipher(cipher: Cipher, plainText: String): EncryptedMessage
    fun initializeCipherDecryption(initializationVector: ByteArray): Cipher
    fun decryptWithCipher(cipher: Cipher, encryptedMessage: EncryptedMessage): String

    fun encrypt(plainText: String): EncryptedMessage
    fun decrypt(encryptedMessage: EncryptedMessage): String

    companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val SYMMETRIC_KEY_ALIAS = "${BuildConfig.KEY_ALIAS}.SYMMETRIC"
        const val ASYMMETRIC_KEY_ALIAS = "${BuildConfig.KEY_ALIAS}.ASYMMETRIC"

        const val RSA_KEY_SIZE = 2048
        const val EC_STD_NAME = "secp256r1"

        const val AES_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        const val RSA_ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA

        const val GCM_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        const val ECB_BLOCK_MODE = KeyProperties.BLOCK_MODE_ECB
        const val NONE_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        const val RSA_OAEP_PADDING = KeyProperties.ENCRYPTION_PADDING_RSA_OAEP
        const val AES_TRANSFORMATION = "$AES_ALGORITHM/$GCM_BLOCK_MODE/$NONE_PADDING"
        const val RSA_TRANSFORMATION = "$RSA_ALGORITHM/$ECB_BLOCK_MODE/$RSA_OAEP_PADDING"
    }
}