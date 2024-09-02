package Rainbow_Frends.global.config

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class ApplicationContextProvider : ApplicationContextAware {
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        ApplicationContextProvider.applicationContext = applicationContext
    }

    companion object {
        lateinit var applicationContext: ApplicationContext
            private set

        fun <T> getBean(beanClass: Class<T>): T {
            return applicationContext.getBean(beanClass)
        }
    }
}