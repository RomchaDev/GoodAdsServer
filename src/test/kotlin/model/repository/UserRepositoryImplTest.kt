package model.repository

import kotlinx.coroutines.runBlocking
import model.datasource.dao.UniversalDAO
import model.datasource.instagram.InstagramDataSourceImpl
import model.entity.token.Token
import model.entity.user.AuthEntity
import model.entity.user.DatabaseUser
import model.entity.user.NetworkUser
import model.token.RandomTokenGenerator
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.persistence.Query

import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserRepositoryImplTest {

    private lateinit var repository: UserRepository
    private lateinit var tokenDAO: UniversalDAO<Token, String>
    private lateinit var factory: SessionFactory

    private val authEntity = AuthEntity(
        username = USERNAME,
        password = PASSWORD,
        postPrice = "300",
        storyPrice = "500",
        cardNumber = "0000 0000 0000 0000"
    )

    private val exampleUser = NetworkUser(
        id = ID,
        nickName = USERNAME,
        name = "Oleg",
        bio = "Fuck you",
        avatarUrl = "",
        posts = 3,
        following = 500,
        followers = 10000,
        postPrice = "300",
        storyPrice = "500",
        cardNumber = "0000 0000 0000 0000"
    )

    @BeforeAll
    fun beforeAll() {
        factory = Configuration().configure().buildSessionFactory()
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

    @BeforeEach
    fun beforeEach() {
        //initHibernate()
        val session = factory.openSession()
        session.beginTransaction()
        val usersQuery: Query = session.createQuery("delete from DatabaseUser")
        val tokensQuery: Query = session.createQuery("DELETE FROM Token")
        usersQuery.executeUpdate()
        tokensQuery.executeUpdate()
        session.transaction.commit()
        session.close()
    }

    private fun setFakeToken() {
        val session = factory.openSession()
        session.beginTransaction()
        val deleteQuery = session.createQuery("DELETE FROM Token")
        deleteQuery.executeUpdate()
        session.transaction.commit()
        session.close()

        tokenDAO.create(Token(TOKEN, ID))
    }

    @Test
    fun getNetworkUserByAuth() = runBlocking {
        repository.saveNetworkUser(exampleUser, authEntity)

        val user = repository.getNetworkUser(authEntity)
        assertEquals(user.bio, "Fuck you")

        repository.saveNetworkUser(user, authEntity)
    }

    @Test
    fun getNetworkUserById() = runBlocking {
        repository.saveNetworkUser(exampleUser, authEntity)

        val user = repository.getNetworkUser(ID)
        assertEquals(user.nickName, USERNAME)
    }

    @Test
    fun getNetworkUserByToken() = runBlocking {
        repository.saveNetworkUser(exampleUser, authEntity)
        setFakeToken()

        val user = repository.getNetworkUser(TOKEN)
        assertEquals(user.nickName, USERNAME)
    }

    @Test
    fun updateNetworkUser() = runBlocking {
        repository.saveNetworkUser(exampleUser, authEntity)

        val user1 = repository.getNetworkUser(ID)

        val p = (Math.random() * 700).toInt().toString()
        val s = (Math.random() * 700).toInt().toString()

        user1.postPrice = p
        user1.storyPrice = s

        repository.updateNetworkUser(user1)

        val user2 = repository.getNetworkUser(ID)

        assertEquals(user2.postPrice, p)
        assertEquals(user2.storyPrice, s)
    }

    @Test
    fun getAllExceptMy() = runBlocking {
        authEntity.username = "Fuck"
        exampleUser.nickName = "Fuck"

        repository.saveNetworkUser(exampleUser, authEntity)
        setFakeToken()

        exampleUser.id = -2
        exampleUser.nickName = USERNAME
        authEntity.username = USERNAME
        repository.saveNetworkUser(exampleUser, authEntity)

        val users = repository.getAllExceptMy(0, 2, TOKEN)

        assertEquals(users.users.size, 1)
        assertEquals(users.users[0].id, ID)

        exampleUser.id = ID
    }

    companion object {
        private const val USERNAME = "ads_testaccount_01"
        private const val PASSWORD = "Lamandrik2005"
        private const val TOKEN = "126088b1-2a8b-4df3-a135-14a5d859f8d7"
        private const val ID = 50340033822
    }
}