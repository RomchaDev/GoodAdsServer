package model.repository

import model.datasource.dao.DAO
import model.datasource.instagram.InstagramDataSource
import model.entity.token.Token
import model.entity.user.AuthEntity
import model.entity.user.DatabaseUser
import model.entity.user.NetworkUser

class UserRepositoryImpl(
    private val localDataSource: DAO<DatabaseUser, Long>,
    private val remoteDataSource: InstagramDataSource,
    private val tokenDataSource: DAO<Token, String>
) : UserRepository {
    override suspend fun getNetworkUser(auth: AuthEntity) =
        remoteDataSource.getNetworkUser(auth)

    override suspend fun getNetworkUser(id: Long): NetworkUser {
        val user = localDataSource.read(id)
        val auth = AuthEntity(
            username = user.username,
            password = user.password,
            postPrice = user.postPrice,
            storyPrice = user.storyPrice,
            cardNumber = user.cardNumber
        )

        return remoteDataSource.getNetworkUser(auth)
    }

    override suspend fun getNetworkUser(tokenStr: String): NetworkUser {
        val token = tokenDataSource.read(tokenStr)
        return getNetworkUser(token.userId)
    }

    override suspend fun saveNetworkUser(user: NetworkUser, auth: AuthEntity) {

        //localDataSource.create()
    }

    override suspend fun getAllExceptMy(start: Int, end: Int, myUserToken: String) {
        TODO("Not yet implemented")
    }
}