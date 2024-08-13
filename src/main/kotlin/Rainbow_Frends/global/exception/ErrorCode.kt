package Rainbow_Frends.global.exception

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(i: Int, s: String) {
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다.")
}