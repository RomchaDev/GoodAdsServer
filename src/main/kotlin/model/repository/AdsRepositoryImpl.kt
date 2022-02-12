package model.repository

import model.datasource.dao.DAO
import model.entity.ad.Ad
import model.entity.ad.Ads
import model.entity.user.DatabaseUser

class AdsRepositoryImpl(
    private val localAdDataSource: DAO<Ad, Long>,
    private val localUserDataSource: DAO<DatabaseUser, Long>
) : AdsRepository {
    override suspend fun createEditAd(ad: Ad) = localAdDataSource.saveOrUpdate(ad)

    override suspend fun getAd(id: Long) = localAdDataSource.read(id)

    override suspend fun deleteAd(id: Long) = localAdDataSource.delete(
        localAdDataSource.read(id)
    )

    override suspend fun getAdsByUserId(userId: Long, start: Int, end: Int): Ads =
        Ads(localUserDataSource.read(userId).ads as List<Ad>)

    override suspend fun getAds(start: Int, end: Int): Ads =
        Ads(localAdDataSource.read(start, end))
}