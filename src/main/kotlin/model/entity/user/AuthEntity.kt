package model.entity.user

data class AuthEntity(
    var username: String,
    val password: String,
    val postPrice: String,
    val storyPrice: String,
    val cardNumber: String?
) {
    companion object {
        fun create(user: DatabaseUser) = with(user) {
            AuthEntity(
                username = user.username,
                password = user.password,
                postPrice = user.postPrice,
                storyPrice = user.storyPrice,
                cardNumber = user.cardNumber
            )
        }
    }
}