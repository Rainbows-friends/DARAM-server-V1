package Rainbow_Frends.domain.notice.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp

@Entity
class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var notice_id: Long? = null
    @Column(nullable = false,length = 50)
    private var title: String? = null
    @Column(nullable = false)
    @Lob
    private var content: String? = null
    @Column(nullable = false)
    private var writer: String? = null
    @Column(nullable = false)
    @CreationTimestamp
    private var createDate: Timestamp? = null
}