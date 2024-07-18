package Rainbow_Frends.domain.GAuth.config;

import gauth.GAuth;
import gauth.impl.GAuthImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GAuthConfig {
    @Bean
    public GAuth gauth() {
        return new GAuthImpl();
    }

    public class Component {
        private final GAuth gAuth;

        public Component(GAuth gAuth) {
            this.gAuth = gAuth;
        }
    }
}