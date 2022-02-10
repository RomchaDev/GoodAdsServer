package model.repository

import model.datasource.dao.DAO
import model.entity.ad.Ad
import model.entity.ad.Ads

class AdsRepositoryImpl(private val localDataSource: DAO<Ad, Long>) : AdsRepository {
    override suspend fun createEditAd(ad: Ad) = localDataSource.saveOrUpdate(ad)

    override suspend fun getAd(id: Long) = localDataSource.read(id)

    override suspend fun deleteAd(id: Long) = localDataSource.delete(
        localDataSource.read(id)
    )

    override suspend fun getAds(start: Int, end: Int): Ads {
        TODO("Not yet implemented")
    }
}