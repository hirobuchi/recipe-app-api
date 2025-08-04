package recipe.app.repository;

import recipe.app.entity.Users;
import recipe.app.entity.WebAuthCredential;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.data.exception.Base64UrlException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MyCredentialRepository implements CredentialRepository {

    private final WebAuthCredentialRepository credentialRepository;
    private final UserRepository userRepository;

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        Optional<Users> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return Set.of();
        }
        List<WebAuthCredential> credList = credentialRepository.findByUserId(user.get().getId());
        return credList.stream()
                .map(c -> {
                    try {
                        return PublicKeyCredentialDescriptor.builder()
                                .id(ByteArray.fromBase64Url(c.getCredentialId()))
                                .build();
                    } catch (Base64UrlException e) {
                        System.err.println("[WARN] Invalid Base64Url credentialId for user " + username);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        return userRepository.findByUsername(username)
                .map(u -> new ByteArray(u.getId().toString().getBytes()));
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        try {
            Long userId = Long.parseLong(new String(userHandle.getBytes()));
            return userRepository.findById(userId).map(Users::getUsername);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        try {
            Long userId = Long.parseLong(new String(userHandle.getBytes()));
            return credentialRepository.findByCredentialId(credentialId.getBase64Url())
                    .filter(c -> c.getUserId().equals(userId))
                    .flatMap(c -> {
                        try {
                            return Optional.of(RegisteredCredential.builder()
                                    .credentialId(credentialId)
                                    .userHandle(userHandle)
                                    .publicKeyCose(ByteArray.fromBase64Url(c.getPublicKey()))
                                    .signatureCount(c.getSignCount())
                                    .build());
                        } catch (Base64UrlException e) {
                            System.err.println("[ERROR] Invalid Base64Url publicKey for credentialId=" + credentialId);
                            return Optional.empty();
                        }
                    });
        } catch (NumberFormatException e) {
            System.err.println("[WARN] Invalid userHandle during lookup: " + new String(userHandle.getBytes()));
            return Optional.empty();
        }
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        return credentialRepository.findByCredentialId(credentialId.getBase64Url())
                .map(c -> {
                    try {
                        return Set.of(RegisteredCredential.builder()
                                .credentialId(credentialId)
                                .userHandle(new ByteArray(c.getUserId().toString().getBytes()))
                                .publicKeyCose(ByteArray.fromBase64Url(c.getPublicKey()))
                                .signatureCount(c.getSignCount())
                                .build());
                    } catch (Base64UrlException e) {
                        System.err.println("[ERROR] Invalid Base64Url publicKey in lookupAll for credentialId=" + credentialId);
                        return Set.<RegisteredCredential>of();
                    }
                })
                .orElse(Set.of());
    }
}
