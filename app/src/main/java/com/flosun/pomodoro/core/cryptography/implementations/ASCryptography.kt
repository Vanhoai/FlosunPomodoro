package com.flosun.pomodoro.core.cryptography.implementations

import android.app.Application
import android.widget.Toast
import com.flosun.pomodoro.BuildConfig
import com.flosun.pomodoro.core.cryptography.BackendAlgorithm
import com.flosun.pomodoro.core.cryptography.Cryptography
import com.flosun.pomodoro.core.cryptography.EncryptedMessage
import com.flosun.pomodoro.core.cryptography.toAlgorithmName
import java.security.KeyStore
import java.security.PublicKey
import javax.crypto.Cipher
import javax.inject.Inject

class ASCryptography @Inject constructor(
    private val application: Application
) : Cryptography {

    private val keyStore = KeyStore.getInstance(Cryptography.ANDROID_KEYSTORE)

    private val publicKey: PublicKey
        get() = keyStore.getCertificate(Cryptography.ASYMMETRIC_KEY_ALIAS).publicKey

    private val privateKey: KeyStore.PrivateKeyEntry
        get() = keyStore.getEntry(
            Cryptography.ASYMMETRIC_KEY_ALIAS,
            null
        ) as KeyStore.PrivateKeyEntry

    init {
        try {
            keyStore.load(null)
            if (!keyStore.containsAlias(Cryptography.ASYMMETRIC_KEY_ALIAS)) createKeyPair()
        } catch (exception: Exception) {
            exception.printStackTrace()
            Toast.makeText(
                application.applicationContext,
                "Failed to load keystore. Encryption and decryption will not work.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @Throws(Exception::class)
    private fun createKeyPair() {
        when (BuildConfig.CRYPTO_ALGORITHM) {
            BackendAlgorithm.RSA.toAlgorithmName() -> createRSAKeyPair()
            BackendAlgorithm.EC.toAlgorithmName() -> createECKeyPair()
            else -> throw IllegalArgumentException("Unsupported algorithm: ${BuildConfig.CRYPTO_ALGORITHM}")
        }
    }

    private fun createRSAKeyPair() {}
    private fun createECKeyPair() {}

    override fun initializeCipherEncryption(): Cipher {
        TODO("Not yet implemented")
    }

    override fun encryptWithCipher(
        cipher: Cipher,
        plainText: String
    ): EncryptedMessage {
        TODO("Not yet implemented")
    }

    override fun initializeCipherDecryption(initializationVector: ByteArray): Cipher {
        TODO("Not yet implemented")
    }

    override fun decryptWithCipher(
        cipher: Cipher,
        encryptedMessage: EncryptedMessage
    ): String {
        TODO("Not yet implemented")
    }

    override fun encrypt(plainText: String): EncryptedMessage {
        TODO("Not yet implemented")
    }

    override fun decrypt(encryptedMessage: EncryptedMessage): String {
        TODO("Not yet implemented")
    }
}