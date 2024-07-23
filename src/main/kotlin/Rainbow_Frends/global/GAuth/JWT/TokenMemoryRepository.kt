package Rainbow_Frends.global.GAuth.JWT

import Rainbow_Frends.global.User.User
import org.springframework.stereotype.Repository

@Repository
class TokenMemoryRepository {
    private val githubTokenStore = mutableMapOf<Long, String>()

    fun saveGithubToken(user: User, token: String) {
        user.id?.let { githubTokenStore[it] = token }
    }

    fun getGithubToken(user: User): String? {
        return user.id?.let { githubTokenStore[it] }
    }
}