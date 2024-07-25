package Rainbow_Frends.domain.User.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.util.*

@Entity
data class User(
    @Id
    @GeneratedValue(generator = "UUID4")
    @GenericGenerator(name = "UUID4", strategy = "uuid4")
    val id: UUID? = null,

    val email: String? = null,

    val name: String? = null,

    @Embedded
    val studentNum: StudentNum? = null,

    @Enumerated(EnumType.STRING)
    val authority: Authority? = null
)