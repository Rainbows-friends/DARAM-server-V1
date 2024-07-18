package Rainbow_Frends.global.GAuth.JWT

import dev.yangsijun.gauth.core.user.GAuthUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class CustomGAuthUser(
    private val id: Long, private val role: String
) : GAuthUser {

    fun getId(): Long {
        return id
    }

    override fun getAttributes(): Map<String, Any> {
        return mapOf("id" to id)
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(role))
    }

    override fun getName(): String {
        return "id"
    }

    override fun <A : Any?> getAttribute(name: String): A? {
        return if (name == "id") id as A else null
    }

}