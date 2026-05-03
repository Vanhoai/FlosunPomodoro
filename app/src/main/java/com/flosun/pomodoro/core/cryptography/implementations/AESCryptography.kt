package com.flosun.pomodoro.core.cryptography.implementations

import android.app.Application
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Toast
import com.flosun.pomodoro.BuildConfig
import com.flosun.pomodoro.core.cryptography.Cryptography
import com.flosun.pomodoro.core.cryptography.EncryptedMessage
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class AESCryptography @Inject constructor(application: Application) : Cryptography {

    private val keystore = KeyStore.getInstance(Cryptography.ANDROID_KEYSTORE)
    private val secretKey: SecretKey
        get() = keystore.getKey(Cryptography.SYMMETRIC_KEY_ALIAS, null) as SecretKey

    init {
        try {
            keystore.load(null)
            // if key alias does not exist, create a new secret key and store it in the keystore
            if (!keystore.containsAlias(Cryptography.SYMMETRIC_KEY_ALIAS)) createSecretKey()
        } catch (exception: Exception) {
            if (BuildConfig.DEBUG) throw exception

            Toast.makeText(
                application.applicationContext,
                "Failed to load keystore. Encryption and decryption will not work.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @Throws(Exception::class)
    private fun createSecretKey() {
        val keyGeneratorParams = KeyGenParameterSpec.Builder(
            Cryptography.SYMMETRIC_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(Cryptography.GCM_BLOCK_MODE)
            setEncryptionPaddings(Cryptography.NONE_PADDING)
        }.build()

        val keyGenerator = KeyGenerator.getInstance(
            Cryptography.AES_ALGORITHM,
            Cryptography.ANDROID_KEYSTORE,
        )

        keyGenerator.init(keyGeneratorParams)
        keyGenerator.generateKey()
    }

    override fun initializeCipherEncryption(): Cipher {
        val cipher = Cipher.getInstance(Cryptography.AES_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }

    override fun encryptWithCipher(cipher: Cipher, plainText: String): EncryptedMessage {
        val bytes = cipher.doFinal(plainText.toByteArray(Charset.forName("UTF-8")))
        return EncryptedMessage(bytes, cipher.iv)
    }

    override fun initializeCipherDecryption(initializationVector: ByteArray): Cipher {
        val cipher = Cipher.getInstance(Cryptography.AES_TRANSFORMATION)

        // NOTE: The initialization vector (IV) is required for GCM mode.
        // You can change GCM to CBC/CTR
        val spec = GCMParameterSpec(128, initializationVector)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        return cipher
    }

    override fun decryptWithCipher(cipher: Cipher, encryptedMessage: EncryptedMessage): String {
        val bytes = cipher.doFinal(encryptedMessage.ciphertext)
        return String(bytes, Charset.forName("UTF-8"))
    }

    override fun encrypt(plainText: String): EncryptedMessage {
        val cipher = initializeCipherEncryption()
        val bytes = cipher.doFinal(plainText.toByteArray(Charset.forName("UTF-8")))
        return EncryptedMessage(bytes, cipher.iv)
    }

    override fun decrypt(encryptedMessage: EncryptedMessage): String {
        val cipher = initializeCipherDecryption(encryptedMessage.extraBytes)
        val bytes = cipher.doFinal(encryptedMessage.ciphertext)
        return String(bytes, Charset.forName("UTF-8"))
    }
}