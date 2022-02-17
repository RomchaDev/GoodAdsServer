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
import org.junit.jupiter.api.BeforeEach
import javax.persistence.Query
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AdsRepositoryImplTest {

    private lateinit var repository: AdsRepository
    private lateinit var userRepository: UserRepository
    private val factory = Configuration().configure().buildSessionFactory()

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

    @BeforeEach
    fun beforeEach() {
        val session = factory.openSession()
        session.beginTransaction()
        val adsQuery: Query = session.createQuery("delete from Ad")
        val idQuery: Query = session.createQuery("ALTER TABLE Ad AUTO_INCREMENT = 1")
        adsQuery.executeUpdate()
        idQuery.executeUpdate()
        session.transaction.commit()
        session.close()
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
            repository.deleteAd(5)

            val ads = repository.getAds(0, 10)
            assertEquals(ads, Ads(listOf()))
        }
    }

    @Test
    fun getAdsByUserId() {
        runBlocking {
            userRepository.saveNetworkUser(startUser, startAuth)
            val ads = repository.getAdsByUserId(123, 0, 1)
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