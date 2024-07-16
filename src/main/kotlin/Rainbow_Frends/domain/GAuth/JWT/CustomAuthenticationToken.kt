package Rainbow_Frends.domain.GAuth.JWT

import dev.yangsijun.gauth.core.user.GAuthUser
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class CustomAuthenticationToken(
    authorities: Collection<GrantedAuthority>, private val principal: GAuthUser
) : AbstractAuthenticationToken(authorities) {

    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Long? {
        return principal.getAttribute<Long>("id")
    }

    override fun getPrincipal(): GAuthUser {
        return principal
    }
}