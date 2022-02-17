package model.entity.user

open class NetworkUser(
    var id: Long,
    var nickName: String,
    val name: String,
    val bio: String,
    val avatarUrl: String,
    val posts: Int,
    val followers: Int,
    val following: Int,
    var postPrice: String,
    var storyPrice: String,
    var cardNumber: String?
)