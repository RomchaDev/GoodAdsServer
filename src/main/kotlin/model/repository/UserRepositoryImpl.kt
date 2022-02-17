package model.repository

import model.datasource.dao.DAO
import model.datasource.instagram.InstagramDataSource
import model.entity.token.Token
import model.entity.user.AuthEntity
import model.entity.user.DatabaseUser
import model.entity.user.NetworkUser
import model.entity.user.Users
import model.token.TokenGenerator

class UserRepositoryImpl(
    private val localUsersDataSource: DAO<DatabaseUser, Long>,
    private val remoteUsersDataSource: InstagramDataSource,
    private val tokenDataSource: DAO<Token, String>,
    private val tokenGenerator: TokenGenerator
) : UserRepository {
    override suspend fun getNetworkUser(auth: AuthEntity) =
        remoteUsersDataSource.getNetworkUser(auth)

    override suspend fun getNetworkUser(id: Long): NetworkUser {
        val user = localUsersDataSource.read(id)
        val auth = AuthEntity.create(user)

        return remoteUsersDataSource.getNetworkUser(auth)
    }

    override suspend fun getNetworkUser(tokenStr: String): NetworkUser {
        val token = tokenDataSource.read(tokenStr)
        return getNetworkUser(token.userId)
    }

    override suspend fun saveNetworkUser(user: NetworkUser, auth: AuthEntity) {
        val databaseUser = DatabaseUser.create(user, auth.password, auth.cardNumber)
        localUsersDataSource.create(databaseUser)

        val token = tokenGenerator.generate()
        tokenDataSource.create(Token(token, user.id))
    }

    override suspend fun updateNetworkUser(user: NetworkUser) {
        val databaseUser = localUsersDataSource.read(user.id)
        databaseUser.cardNumber = user.cardNumber
        databaseUser.username = user.nickName
        databaseUser.postPrice = user.postPrice
        databaseUser.storyPrice = user.storyPrice
        localUsersDataSource.update(databaseUser)
    }

    override suspend fun getAllExceptMy(
        start: Int,
        end: Int,
        myUserToken: String
    ): Users {
        val users = localUsersDataSource.read(start, end).toMutableList()
        val id = tokenDataSource.read(myUserToken).userId
        users.removeIf {
            val res = it.id == id
            res
        }

        val list =  users.map {
            val auth = AuthEntity.create(it)
            remoteUsersDataSource.getNetworkUser(auth)
        }

        return Users(list)
    }
}
