package Rainbow_Frends.global.exception

import com.fasterxml.jackson.annotation.JsonFormat


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(val status: Short, val message: String) {

    USER_NOT_FOUND(404, "User를 찾을 수 없습니다."),
    EXPIRED_TOKEN(401, "Token이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN(401, "만료된 Refresh Token 입니다."),
    INVALID_TOKEN_TYPE(401, "유효하지 않은 Token 타입입니다."),
    INVALID_TOKEN(401, "유효하지 않은 Token 입니다.")
}