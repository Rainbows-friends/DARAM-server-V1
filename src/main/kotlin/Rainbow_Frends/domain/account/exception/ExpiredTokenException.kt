package Rainbow_Frends.domain.account.exception

import Rainbow_Frends.global.exception.DARAMException
import Rainbow_Frends.global.exception.ErrorCode

class ExpiredTokenException : DARAMException(ErrorCode.EXPIRED_TOKEN)