package Rainbow_Frends

import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.SecurityCollection
import org.apache.tomcat.util.descriptor.web.SecurityConstraint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SecurityApplication
fun main(args: Array<String>) {
    runApplication<SecurityApplication>(*args)
}

@Bean
fun servletContainer(): ServletWebServerFactory {
    val tomcat = object : TomcatServletWebServerFactory() {
        override fun postProcessContext(context: Context) {
            val securityConstraint = SecurityConstraint()
            securityConstraint.userConstraint = "CONFIDENTIAL"
            val collection = SecurityCollection()
            collection.addPattern("/*")
            securityConstraint.addCollection(collection)
            context.addConstraint(securityConstraint)
        }
    }

    tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector())
    return tomcat
}

private fun httpToHttpsRedirectConnector(): Connector {
    val connector = Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
    connector.scheme = "http"
    connector.port = 5000
    connector.secure = false
    connector.redirectPort = 443
    return connector
}