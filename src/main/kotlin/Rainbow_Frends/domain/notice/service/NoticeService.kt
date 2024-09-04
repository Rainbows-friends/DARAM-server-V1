package Rainbow_Frends.domain.notice.service

import Rainbow_Frends.domain.account.entity.Account
import Rainbow_Frends.domain.notice.dto.request.NoticeRequest
import Rainbow_Frends.domain.notice.entity.Notice

interface NoticeService {
    fun readAllNotice(): List<Notice>
    fun findNoticeById(id: Long): Notice?
    fun deleteNotice(id: Long): Boolean
    fun updateNotice(id: Long, noticeRequest: NoticeRequest, account: Account): Notice?
}