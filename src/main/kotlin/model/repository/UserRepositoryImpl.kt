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
    private val localDataSource: DAO<DatabaseUser, Long>,
    private val remoteDataSource: InstagramDataSource,
    private val tokenDataSource: DAO<Token, String>,
    private val tokenGenerator: TokenGenerator
) : UserRepository {
    override suspend fun getNetworkUser(auth: AuthEntity) =
        remoteDataSource.getNetworkUser(auth)

    override suspend fun getNetworkUser(id: Long): NetworkUser {
        val user = localDataSource.read(id)
        val auth = AuthEntity.create(user)

        return remoteDataSource.getNetworkUser(auth)
    }

    override suspend fun getNetworkUser(tokenStr: String): NetworkUser {
        val token = tokenDataSource.read(tokenStr)
        return getNetworkUser(token.userId)
    }

    override suspend fun saveNetworkUser(user: NetworkUser, auth: AuthEntity) {
        val databaseUser = DatabaseUser.create(user, auth.password, auth.cardNumber)
        val id = localDataSource.create(databaseUser) as Long
        val token = tokenGenerator.generate()
        tokenDataSource.create(Token(token, id))
    }

    override suspend fun updateNetworkUser(user: NetworkUser) {
        val databaseUser = localDataSource.read(user.id)
        databaseUser.cardNumber = user.cardNumber
        databaseUser.username = user.nickName
        databaseUser.postPrice = user.postPrice
        databaseUser.storyPrice = user.storyPrice
        localDataSource.update(databaseUser)
    }

    override suspend fun getAllExceptMy(
        start: Int,
        end: Int,
        myUserToken: String
    ): Users {
        val users = localDataSource.read(start, end)
        val id = tokenDataSource.read(myUserToken).userId
        users.toMutableList().removeAll { it.id == id }

        val list =  users.map {
            val auth = AuthEntity.create(it)
            remoteDataSource.getNetworkUser(auth)
        }

        return Users(list)
    }
}