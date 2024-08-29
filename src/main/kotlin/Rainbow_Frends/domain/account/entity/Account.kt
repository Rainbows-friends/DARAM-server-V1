package Rainbow_Frends.domain.account.entity

import jakarta.persistence.*

@Entity
class Account {
    @Id
    @Column(name = "student_id", unique = true, nullable = false)
    var student_id: Int? = null

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role? = null
}