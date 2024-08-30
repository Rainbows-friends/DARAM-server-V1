package Rainbow_Frends.domain.notice.entity

import Rainbow_Frends.domain.account.entity.Account
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp

@Entity
class Notice(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var notice_id: Long? = null,

    @Column(nullable = false, length = 50) var title: String,

    @Column(nullable = false) @Lob var content: String,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(
        name = "writer",
        referencedColumnName = "studentId",
        nullable = false
    ) var writer: Account,

    @Column(nullable = false) @CreationTimestamp var createDate: Timestamp? = null
)