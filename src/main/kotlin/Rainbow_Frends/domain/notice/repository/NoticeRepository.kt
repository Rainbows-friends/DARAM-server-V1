package Rainbow_Frends.domain.notice.repository

import Rainbow_Frends.domain.notice.entity.Notice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository : JpaRepository<Notice, Long>