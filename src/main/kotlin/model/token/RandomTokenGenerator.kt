package model.token

import java.security.SecureRandom

class RandomTokenGenerator : TokenGenerator {
    override fun generate(): String {
        val random = SecureRandom()
        val bytes = ByteArray(20)
        random.nextBytes(bytes)
        return bytes.toString()
    }
}