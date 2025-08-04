package recipe.app.service;

import com.yubico.webauthn.data.exception.Base64UrlException;
import recipe.app.dto.AuthenticationResponseDto;
import recipe.app.dto.RegistrationResponseDto;
import recipe.app.entity.Users;
import recipe.app.entity.WebAuthChallenge;
import recipe.app.entity.WebAuthCredential;
import recipe.app.repository.UserRepository;
import recipe.app.repository.WebAuthChallengeRepository;
import recipe.app.repository.WebAuthCredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WebAuthService {

    private final RelyingParty relyingParty;
    private final WebAuthCredentialRepository credentialRepository;
    private final WebAuthChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public PublicKeyCredentialCreationOptions startRegistration(String username) {
        // 安全なチャレンジ生成
        byte[] challengeBytes = new byte[32];
        new SecureRandom().nextBytes(challengeBytes);
        ByteArray challenge = new ByteArray(challengeBytes);

        // DBにチャレンジを保存（Base64URL形式）
        WebAuthChallenge challengeEntity = new WebAuthChallenge();
        challengeEntity.setChallenge(challenge.getBase64Url());
        challengeEntity.setExpiresAt(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)));
        challengeRepository.save(challengeEntity);

        // ユーザIDはDBのIDをByteArray化するのが理想（ここではusernameを仮ID化）
        ByteArray userId = new ByteArray(username.getBytes());

        return PublicKeyCredentialCreationOptions.builder()
                .rp(relyingParty.getIdentity())
                .user(UserIdentity.builder()
                        .name(username)
                        .displayName(username)
                        .id(userId)
                        .build())
                .challenge(challenge)
                .pubKeyCredParams(List.of(
                        PublicKeyCredentialParameters.builder()
                                .alg(COSEAlgorithmIdentifier.ES256)
                                .type(PublicKeyCredentialType.PUBLIC_KEY)
                                .build()))
                .timeout(60000L)
                .build();
    }

    public void finishRegistration(RegistrationResponseDto response) {
        // TODO: YubicoのRegistrationResultで認証を検証する実装を追加
        WebAuthCredential credential = new WebAuthCredential();
        credential.setUserId(1L); // TODO: ユーザIDと紐付け
        credential.setCredentialId(response.getCredentialId());
        credential.setPublicKey(response.getPublicKey());
        credentialRepository.save(credential);
    }

    public PublicKeyCredentialRequestOptions startAuthentication(String username) {
        // ユーザを取得
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // チャレンジ生成
        byte[] challengeBytes = new byte[32];
        new SecureRandom().nextBytes(challengeBytes);
        ByteArray challenge = new ByteArray(challengeBytes);

        // DBにチャレンジを保存
        WebAuthChallenge challengeEntity = new WebAuthChallenge();
        challengeEntity.setChallenge(challenge.getBase64Url());
        challengeEntity.setExpiresAt(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)));
        challengeRepository.save(challengeEntity);

        // ユーザの資格情報を取得
        List<WebAuthCredential> userCredentials = credentialRepository.findByUserId(users.getId());
        List<PublicKeyCredentialDescriptor> allowCredentials = new ArrayList<>();

        for (WebAuthCredential cred : userCredentials) {
            try {
                allowCredentials.add(PublicKeyCredentialDescriptor.builder()
                        .id(ByteArray.fromBase64Url(cred.getCredentialId()))
                        .type(PublicKeyCredentialType.PUBLIC_KEY)
                        .build());
            } catch (Base64UrlException e) {
                System.err.println("Invalid Base64Url credentialId for user " + username + ": " + cred.getCredentialId());
                // 不正データはスキップ
            }
        }

        return PublicKeyCredentialRequestOptions.builder()
                .challenge(challenge)
                .timeout(60000L)
                .rpId(relyingParty.getIdentity().getId())
                .allowCredentials(allowCredentials)
                .userVerification(UserVerificationRequirement.PREFERRED)
                .build();
    }

    public String finishAuthentication(AuthenticationResponseDto response) {
        // TODO: 実際にはAuthenticatorAssertionResponseの検証を行う
        return jwtService.generateToken("username");
    }
}