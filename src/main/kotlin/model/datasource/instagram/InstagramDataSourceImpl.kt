package model.datasource.instagram

import model.entity.user.AuthEntity
import model.entity.user.NetworkUser
import org.brunocvcunha.instagram4j.Instagram4j
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest

class InstagramDataSourceImpl : InstagramDataSource {
    override suspend fun getNetworkUser(
        authEntity: AuthEntity,
    ): NetworkUser {
        val instagram = Instagram4j
            .builder()
            .username(authEntity.username)
            .password(authEntity.password)
            .build()

        instagram.setup()
        instagram.login()

        return networkUserFromInstagram4j(authEntity, instagram)
    }

    private fun networkUserFromInstagram4j(
        authEntity: AuthEntity,
        instagram4j: Instagram4j
    ): NetworkUser {

        val instagramUser = instagram4j
            .sendRequest(InstagramSearchUsernameRequest(authEntity.username)).user

        return with(instagramUser) {
            NetworkUser(
                id = instagramUser.pk,
                nickName = username,
                name = full_name,
                bio = biography,
                avatarUrl = profile_pic_url,
                posts = media_count,
                followers = follower_count,
                following = following_count,
                postPrice = authEntity.postPrice,
                storyPrice = authEntity.storyPrice,
                cardNumber = authEntity.cardNumber
            )
        }
    }
}
