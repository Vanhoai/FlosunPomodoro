package com.flosun.pomodoro.core.cryptography

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.nio.charset.Charset
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.ECGenParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton


/**
 * A simple password hasher that can be used for demonstration purposes.
 * In a real application, you should use a secure hashing algorithm like bcrypt or Argon2.
 *
 * In this sample, I will use symmetric encryption (AES) to hash the password, and EC keys to verify the password.
 * Hash and store password process:
 * 1. Generate AES key for encryption and EC key for verification.
 * 2. Encrypt the password using AES and store combined message (AES Key + IV + Encrypted Message).
 * 3. Sign the password using EC key and store the signature in the database.
 *
 * Retrieve and verify password process:
 * 1. Retrieve the combined message (AES Key + IV + Encrypted Message) and the signature from the database.
 * 2. Decrypt the password using AES and verify the signature using EC key.
 */

@Singleton
class PasswordHasher @Inject constructor() {
    private val json = Json { ignoreUnknownKeys = true }
    private val keyStore = KeyStore.getInstance(Cryptography.ANDROID_KEYSTORE)
    private val publicKey: PublicKey
        get() = keyStore.getCertificate(Cryptography.ASYMMETRIC_KEY_ALIAS).publicKey

    private val privateKeyEntry: KeyStore.PrivateKeyEntry
        get() = keyStore.getEntry(
            Cryptography.ASYMMETRIC_KEY_ALIAS,
            null
        ) as KeyStore.PrivateKeyEntry

    private val privateKey: PrivateKey
        get() = privateKeyEntry.privateKey

    init {
        try {
            keyStore.load(null)
            if (!keyStore.containsAlias(Cryptography.ASYMMETRIC_KEY_ALIAS)) createECKeyPair()
            else Timber.tag("EncryptStorage").d("Asymmetric key pair already exists")
        } catch (exception: Exception) {
            Timber.e(exception, "Failed to initialize KeyStore")
        }
    }

    @Throws(Exception::class)
    private fun createECKeyPair() {
        val keyGenParams = KeyGenParameterSpec.Builder(
            Cryptography.ASYMMETRIC_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT or
                    KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).apply {
            setAlgorithmParameterSpec(ECGenParameterSpec(Cryptography.EC_STD_NAME))
            setDigests(KeyProperties.DIGEST_SHA256)

            // Authentication settings
            setUserAuthenticationRequired(false)
        }.build()

        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            Cryptography.ANDROID_KEYSTORE
        )
        keyPairGenerator.initialize(keyGenParams)
        keyPairGenerator.generateKeyPair()
    }

    private fun signWithEC(value: ByteArray): ByteArray {
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initSign(privateKey)
        signature.update(value)
        return signature.sign()
    }

    private fun verifyWithEC(value: ByteArray, signatureBytes: ByteArray): Boolean {
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initVerify(publicKey)
        signature.update(value)
        return signature.verify(signatureBytes)
    }

    fun hash(password: String): String {
        // Generate AES key for encryption
        val aesKeyGen = KeyGenerator.getInstance(Cryptography.AES_ALGORITHM)
        aesKeyGen.init(256)
        val aesKey = aesKeyGen.generateKey()

        // Encrypt the password using AES
        val aesCipher = Cipher.getInstance(Cryptography.AES_TRANSFORMATION)
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey)
        val encryptedBytes = aesCipher.doFinal(password.toByteArray(Charset.forName("UTF-8")))
        val iv = aesCipher.iv

        // Combine AES Key + IV + Encrypted Message
        val combinedMessage = ByteArray(aesKey.encoded.size + iv.size + encryptedBytes.size)
        System.arraycopy(aesKey.encoded, 0, combinedMessage, 0, aesKey.encoded.size)
        System.arraycopy(iv, 0, combinedMessage, aesKey.encoded.size, iv.size)
        System.arraycopy(
            encryptedBytes,
            0,
            combinedMessage,
            aesKey.encoded.size + iv.size,
            encryptedBytes.size
        )

        val signature = signWithEC(combinedMessage)
        val response = json.encodeToString(
            value = EncryptedMessage(
                ciphertext = combinedMessage,
                extraBytes = signature,
            ),
            serializer = EncryptedMessage.serializer()
        )

        return response
    }

    fun verify(password: String, hashedPassword: String): Boolean {
        // Decode the hashed password from JSON
        val hashedPassword = json.decodeFromString(
            deserializer = EncryptedMessage.serializer(),
            string = hashedPassword
        )
        
        // Verify the signature using EC key
        val isSignatureValid = verifyWithEC(hashedPassword.ciphertext, hashedPassword.extraBytes)
        if (!isSignatureValid) return false

        // Extract Combined Message and Signature
        val aesKeySize = 32 // AES-256 key size in bytes
        val ivSize = 12 // GCM IV size in bytes

        val value = hashedPassword.ciphertext
        val aesKeyEncoded = value.sliceArray(0 until aesKeySize)
        val iv = value.sliceArray(aesKeySize until aesKeySize + ivSize)
        val encryptedPassword = value.sliceArray(aesKeySize + ivSize until value.size)

        val aesKey = SecretKeySpec(aesKeyEncoded, Cryptography.AES_ALGORITHM)
        val aesCipher = Cipher.getInstance(Cryptography.AES_TRANSFORMATION)
        val spec = GCMParameterSpec(128, iv)
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, spec)
        val decryptedBytes = aesCipher.doFinal(encryptedPassword)

        val decryptedPassword = String(decryptedBytes, Charset.forName("UTF-8"))
        return decryptedPassword == password
    }
}