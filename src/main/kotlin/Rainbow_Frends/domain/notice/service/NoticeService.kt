package Rainbow_Frends.domain.notice.service

import Rainbow_Frends.domain.notice.entity.Notice

interface NoticeService {
    fun readAllNotice(): List<Notice>
}