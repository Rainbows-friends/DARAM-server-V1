package Rainbow_Frends.global.auth

import Rainbow_Frends.domain.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class AuthDetails(private val user: User) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val simpleGrantedAuthority = SimpleGrantedAuthority(user.authority?.name ?: "ROLE_USER")
        return listOf(simpleGrantedAuthority)
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String? {
        return user.email
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return false
    }
}