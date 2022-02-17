package model.token

import java.util.*

class RandomTokenGenerator : TokenGenerator {
    override fun generate() = UUID.randomUUID().toString();
}