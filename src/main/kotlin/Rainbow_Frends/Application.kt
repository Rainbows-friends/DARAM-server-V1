package Rainbow_Frends

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["Rainbow_Frends.domain.account.repository.jpa", "Rainbow_Frends.domain.User.repository", "Rainbow_Frends.domain.notice.repository"])
@EnableRedisRepositories(basePackages = ["Rainbow_Frends.domain.account.repository.redis"])
class SecurityApplication

fun main(args: Array<String>) {
    runApplication<SecurityApplication>(*args)
}