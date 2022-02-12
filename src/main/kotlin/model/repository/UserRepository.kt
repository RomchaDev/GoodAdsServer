package model.repository

import model.entity.user.AuthEntity
import model.entity.user.NetworkUser

interface UserRepository {
    suspend fun getNetworkUser(auth: AuthEntity): NetworkUser

    suspend fun getNetworkUser(id: Long): NetworkUser

    suspend fun getNetworkUser(tokenStr: String): NetworkUser

    suspend fun saveNetworkUser(user: NetworkUser, auth: AuthEntity)

    suspend fun updateNetworkUser(user: NetworkUser)

    suspend fun getAllExceptMy(
        start: Int,
        end: Int,
        myUserToken: String
    ): List<NetworkUser>
}