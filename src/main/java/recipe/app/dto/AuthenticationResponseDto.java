package recipe.app.dto;

import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import lombok.Data;

@Data
public class AuthenticationResponseDto {
    private String requestId;
    private PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> response;
}
