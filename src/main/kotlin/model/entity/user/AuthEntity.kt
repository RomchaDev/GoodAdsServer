package model.entity.user

data class AuthEntity(
    val username: String,
    val password: String,
    val postPrice: String,
    val storyPrice: String,
    val cardNumber: String?
)