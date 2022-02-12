package model.repository

import kotlinx.coroutines.runBlocking
import model.datasource.dao.UniversalDAO
import model.datasource.instagram.InstagramDataSourceImpl
import model.entity.token.Token
import model.entity.user.AuthEntity
import model.entity.user.DatabaseUser
import model.entity.user.NetworkUser
import model.token.RandomTokenGenerator
import org.hibernate.cfg.Configuration
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

internal class UserRepositoryImplTest {

    private lateinit var repository: UserRepository
    private lateinit var tokenDAO: UniversalDAO<Token, String>

    private val authEntity = AuthEntity(
        username = USERNAME,
        password = PASSWORD,
        postPrice = "300",
        storyPrice = "500",
        cardNumber = "0000 0000 0000 0000"
    )

    @BeforeTest
    fun beforeAll() {
        val factory = Configuration().configure().buildSessionFactory()
        val usersDAO = UniversalDAO<DatabaseUser, Long>(factory, DatabaseUser::class.java)
        val tokenDAO = UniversalDAO<Token, String>(factory, Token::class.java)
        val remoteDataSource = InstagramDataSourceImpl()
        val tokenGenerator = RandomTokenGenerator()
        this.tokenDAO = tokenDAO

        repository = UserRepositoryImpl(
            localUsersDataSource = usersDAO,
            remoteUsersDataSource = remoteDataSource,
            tokenDataSource = tokenDAO,
            tokenGenerator = tokenGenerator
        )
    }

    @Test
    fun getNetworkUserByAuth() {
        runBlocking {

            val user = repository.getNetworkUser(authEntity)
            assertEquals(user.bio, "Fuck you")

            repository.saveNetworkUser(user, authEntity)
        }
    }

    @Test
    fun getNetworkUserById() {

    }

    @Test
    fun getNetworkUserByToken() {
    }

    @Test
    fun updateNetworkUser() {
    }

    @Test
    fun getAllExceptMy() {
    }

    companion object {
        private const val USERNAME = "ads_testaccount_01"
        private const val PASSWORD = "Lamandrik2005"
        private var token = ""
    }
}