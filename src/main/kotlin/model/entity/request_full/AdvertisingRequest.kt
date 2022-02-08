package model.entity.request_full

import model.entity.ad.Ad
import model.entity.user.NetworkUser
import javax.persistence.*

@Entity
@Table(name = "requests")
class AdvertisingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var requestId: Long = 0

    @Column
    @OneToOne
    @JoinColumn(name = "adId")
    var ad: Ad? = null

    @Column
    @OneToOne
    @JoinColumn(name = "userId")
    var user: NetworkUser? = null

    @Column var price: Int = 0
}