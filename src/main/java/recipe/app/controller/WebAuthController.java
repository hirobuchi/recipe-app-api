package recipe.app.controller;

import recipe.app.dto.AuthenticationResponseDto;
import recipe.app.dto.RegistrationResponseDto;
import recipe.app.service.WebAuthService;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.PublicKeyCredentialRequestOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class WebAuthController {

    private final WebAuthService webAuthService;

    @PostMapping("/register/start")
    public PublicKeyCredentialCreationOptions startRegister(@RequestBody Map<String, String> body) {
        return webAuthService.startRegistration(body.get("username"));
    }

    @PostMapping("/register/finish")
    public ResponseEntity<?> finishRegister(@RequestBody RegistrationResponseDto response) {
        webAuthService.finishRegistration(response);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/authenticate/start")
    public PublicKeyCredentialRequestOptions startAuthenticate(@RequestBody Map<String, String> body) {
        return webAuthService.startAuthentication(body.get("username"));
    }

    @PostMapping("/authenticate/finish")
    public ResponseEntity<?> finishAuthenticate(@RequestBody AuthenticationResponseDto response) {
        String jwt = webAuthService.finishAuthentication(response);
        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
