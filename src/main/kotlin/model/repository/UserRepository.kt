package model.repository

import model.entity.user.AuthEntity
import model.entity.user.NetworkUser

interface UserRepository {
    suspend fun getNetworkUser(auth: AuthEntity): NetworkUser
}