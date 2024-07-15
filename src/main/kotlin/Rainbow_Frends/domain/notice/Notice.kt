package Rainbow_Frends.domain.notice

import jakarta.persistence.*

@Entity
class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var notice_id: Long? = null
    @Column(nullable = false,length = 50)
    private var title: String? = null
    @Column(nullable = false)
    private var content: String? = null
    @Column(nullable = false)
    private var writer: String? = null
}