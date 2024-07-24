package Rainbow_Frends.global.GAuth.JWT

import org.springframework.security.core.AuthenticationException

class CustomAuthenticationException(msg: String) : AuthenticationException(msg)