package Rainbow_Frends.domain.account.entity

import jakarta.persistence.*

@Entity
class Account {
    @Id
    @Column(name = "studentId", unique = true, nullable = false)
    var studentId: Int? = null

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role? = null
}