package model.repository

import model.datasource.dao.DAO
import model.entity.ad.Ad
import model.entity.ad.Ads
import model.entity.user.DatabaseUser

class AdsRepositoryImpl(
    private val localAdsDataSource: DAO<Ad, Long>,
    private val localUsersDataSource: DAO<DatabaseUser, Long>
) : AdsRepository {
    override suspend fun createEditAd(ad: Ad) = localAdsDataSource.saveOrUpdate(ad)

    override suspend fun getAd(id: Long) = localAdsDataSource.read(id)

    override suspend fun deleteAd(id: Long) = localAdsDataSource.delete(
        localAdsDataSource.read(id)
    )

    override suspend fun getAdsByUserId(userId: Long, start: Int, end: Int): Ads =
        Ads(localUsersDataSource.read(userId).ads as List<Ad>)

    override suspend fun getAds(start: Int, end: Int): Ads =
        Ads(localAdsDataSource.read(start, end))
}