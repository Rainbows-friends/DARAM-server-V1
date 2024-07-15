package Rainbow_Frends.domain.GAuth

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class GAuth(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val profileUrl: String? = null,
    val email: String? = null,
    val name: String? = null,
    val gender: String? = null,
    val grade: Int? = null,
    val classNum: Int? = null,
    val num: Int? = null,
    val role: String? = null
)