package model.entity.user

import model.entity.ad.Ad
import project_utils.NoArg
import javax.persistence.*

@Entity
@NoArg
@Table(name = "users")
open class DatabaseUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false) var id: Long,
    @Column var username: String,
    @Column var password: String,
    @Column var postPrice: String,
    @Column var storyPrice: String,
    @Column var cardNumber: String?,
    @OneToMany
    @Column var ads: MutableList<Ad>? = null
) {
    companion object {
        fun create(
            source: NetworkUser,
            password: String,
            cardNumber: String?
        ) = with(source) {
            DatabaseUser(
                id = id,
                username = nickName,
                password = password,
                postPrice = postPrice,
                storyPrice = storyPrice,
                cardNumber = cardNumber
            )
        }
    }
}