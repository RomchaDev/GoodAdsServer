package model.datasource.instagram

import model.entity.user.AuthEntity
import model.entity.user.NetworkUser

interface InstagramDataSource {
    suspend fun getNetworkUser(
        authEntity: AuthEntity
    ): NetworkUser
}