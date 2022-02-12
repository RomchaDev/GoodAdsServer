package model.repository

import kotlinx.coroutines.runBlocking
import model.datasource.dao.UniversalDAO
import model.entity.ad.Ad
import model.entity.ad.Ads
import model.entity.user.DatabaseUser
import org.hibernate.cfg.Configuration
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AdsRepositoryImplTest {

    private lateinit var repository: AdsRepository

    private val startAd = Ad(
        1,
        "user",
        "300$",
        420,
        69
    )

    private val startAd2 = Ad(
        2,
        "user2",
        "300$",
        420,
        69
    )

    @BeforeTest
    fun beforeAll() {
        val factory = Configuration().configure().buildSessionFactory()
        val adsDAO = UniversalDAO<Ad, Long>(factory, Ad::class.java)
        val usersDAO = UniversalDAO<DatabaseUser, Long>(factory, DatabaseUser::class.java)

        repository = AdsRepositoryImpl(
            localAdsDataSource = adsDAO, localUsersDataSource = usersDAO
        )
    }

    @Test
    fun createEditAd() {
        runBlocking {
            repository.createEditAd(startAd)
            val ad = repository.getAd(1)
            assertEquals(ad.price, "300$")
        }
    }

    @Test
    fun deleteAd() {
        runBlocking {
            repository.createEditAd(startAd)
            repository.deleteAd(1)

            val ads = repository.getAds(0, 10)
            assertEquals(ads, Ads(listOf()))
        }
    }

    @Test
    fun getAdsByUserId() {
        runBlocking {

        }
    }

    @Test
    fun getAds() {
        runBlocking {
            repository.createEditAd(startAd)
            repository.createEditAd(startAd2)

            val ads = repository.getAds(0, 10)
            assertEquals(ads, Ads(listOf(startAd, startAd2)))
        }
    }
}