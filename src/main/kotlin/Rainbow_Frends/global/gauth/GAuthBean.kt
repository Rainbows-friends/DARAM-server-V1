package Rainbow_Frends.global.gauth

import gauth.GAuth
import gauth.impl.GAuthImpl
import org.springframework.beans.factory.annotation.Configurable

@Configurable
class GAuthBean {
    fun gauth(): GAuth {
        return GAuthImpl()
    }
}