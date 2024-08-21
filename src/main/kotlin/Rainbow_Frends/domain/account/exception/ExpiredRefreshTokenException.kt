package Rainbow_Frends.domain.account.exception

import Rainbow_Frends.global.exception.DARAMException
import Rainbow_Frends.global.exception.ErrorCode

class ExpiredRefreshTokenException : DARAMException(ErrorCode.EXPIRED_REFRESH_TOKEN)