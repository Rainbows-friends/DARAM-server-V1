package Rainbow_Frends.domain.checkin.entity

import Rainbow_Frends.domain.User.entity.User
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "checkin_info")
data class Checkin(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false) val user: User,
    @Column(nullable = false) val checkintruefalse: Boolean,
    @Column(nullable = false) val checkinDate: LocalDate
)