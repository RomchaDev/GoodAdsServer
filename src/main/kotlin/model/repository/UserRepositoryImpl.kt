package model.repository

import model.dao.DAO
import model.datasource.instagram.InstagramDataSource
import model.entity.user.AuthEntity
import model.entity.user.DatabaseUser

class UserRepositoryImpl(
    private val localDataSource: DAO<DatabaseUser, Long>,
    private val remoteDataSource: InstagramDataSource
): UserRepository {
    override suspend fun getNetworkUser(auth: AuthEntity) =
        remoteDataSource.getNetworkUser(auth)
}