package model.entity.token

import project_utils.NoArg
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@NoArg
@Table(name = "tokens")
open class Token(
    @Id
    @Column val token: String,
    @Column val userId: Long
)