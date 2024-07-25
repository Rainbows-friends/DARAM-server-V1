package Rainbow_Frends.domain.account.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Account {
    @Id
    @Column(name = "student_id", unique = true, nullable = false)
    private var studen_id: Int? = null

    @Column(name = "role", nullable = false)
    private var role: Role? = null
}