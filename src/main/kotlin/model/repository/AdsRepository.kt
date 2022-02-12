package model.repository

import model.entity.ad.Ad
import model.entity.ad.Ads

interface AdsRepository {
    suspend fun createEditAd(ad: Ad)

    suspend fun getAd(id: Long) : Ad

    suspend fun deleteAd(id: Long)

    suspend fun getAds(start: Int, end: Int) : Ads


}