package model.entity.user

open class NetworkUser(
    val id: Long,
    val nickName: String,
    val name: String,
    val bio: String,
    val avatarUrl: String,
    val posts: Int,
    val followers: Int,
    val following: Int,
    val postPrice: String,
    val storyPrice: String,
    val cardNumber: String?
)