package recipe.app.config;

import recipe.app.repository.MyCredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebAuthConfig {

    @Bean
    public RelyingParty relyingParty(MyCredentialRepository credentialRepository,
                                     @Value("${auth.rp.id:localhost}") String rpId,
                                     @Value("${auth.rp.name:Recipe App}") String rpName) {
        return RelyingParty.builder()
                .identity(RelyingPartyIdentity.builder()
                        .id(rpId)
                        .name(rpName)
                        .build())
                .credentialRepository(credentialRepository)
                .build();
    }
}
