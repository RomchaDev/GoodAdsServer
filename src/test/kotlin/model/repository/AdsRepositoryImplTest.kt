package model.repository

import kotlinx.coroutines.runBlocking
import model.datasource.dao.UniversalDAO
import model.datasource.instagram.InstagramDataSourceImpl
import model.entity.ad.Ad
import model.entity.ad.Ads
import model.entity.token.Token
import model.entity.user.AuthEntity
import model.entity.user.DatabaseUser
import model.entity.user.NetworkUser
import model.token.RandomTokenGenerator
import org.hibernate.cfg.Configuration
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AdsRepositoryImplTest {

    private lateinit var repository: AdsRepository
    private lateinit var userRepository: UserRepository

    private val startAd = Ad(
        id = null,
        userId = 123,
        price = "300$",
        places = 420,
        occupiedPlaces = 69
    )

    private val startAd2 = Ad(
        null,
        124,
        "300$",
        420,
        69
    )

    private val startUser = NetworkUser(
        123,
        "nick",
        "name",
        "bio",
        "url",
        10,
        100,
        12,
        "200$",
        "1$",
        "12345678"
    )

    private val startAuth = AuthEntity(
        "username",
        "password",
        "200$",
        "1$",
        "12345678"
    )

    @BeforeTest
    fun beforeAll() {
        val factory = Configuration().configure().buildSessionFactory()
        val adsDAO = UniversalDAO<Ad, Long>(factory, Ad::class.java)
        val usersDAO = UniversalDAO<DatabaseUser, Long>(factory, DatabaseUser::class.java)

        repository = AdsRepositoryImpl(
            localAdsDataSource = adsDAO, localUsersDataSource = usersDAO
        )

        val tokenDAO = UniversalDAO<Token, String>(factory, Token::class.java)
        val remoteDataSource = InstagramDataSourceImpl()
        val tokenGenerator = RandomTokenGenerator()

        userRepository = UserRepositoryImpl(
            localUsersDataSource = usersDAO,
            remoteUsersDataSource = remoteDataSource,
            tokenDataSource = tokenDAO,
            tokenGenerator = tokenGenerator
        )
    }

    @Test
    fun createEditAd() {
        runBlocking {
            repository.createEditAd(startAd)
            val ad = repository.getAd(14)
            assertEquals(ad.price, "300$")
        }
    }

    @Test
    fun deleteAd() {
        runBlocking {
            repository.createEditAd(startAd)
            repository.deleteAd(9)

            val ads = repository.getAds(0, 10)
            assertEquals(ads, Ads(listOf()))
        }
    }

    @Test
    fun getAdsByUserId() {
        runBlocking {
            userRepository.saveNetworkUser(startUser, startAuth)
            val ads = repository.getAdsByUserId(1, 0, 1)
            assertEquals(ads.adsList[0].price, "300$")
        }
    }

    @Test
    fun getAds() {
        runBlocking {
            repository.createEditAd(startAd)
            repository.createEditAd(startAd2)

            val ads = repository.getAds(0, 10)
            assertEquals(ads.adsList[0].userId, 123)
        }
    }
}