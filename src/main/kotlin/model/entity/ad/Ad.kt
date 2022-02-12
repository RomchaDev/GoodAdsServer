package model.entity.ad

import project_utils.NoArg
import javax.persistence.*

@Entity
@Table(name = "ads")
@NoArg
open class Ad(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "id", nullable = false) var id: Long?,
    @Column var userId: String?,
    @Column var price: String?,
    @Column var places: Int?,
    @Column var occupiedPlaces: Int?,
)