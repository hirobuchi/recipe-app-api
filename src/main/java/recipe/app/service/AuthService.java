package recipe.app.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import recipe.app.entity.AuthCredentials;
import recipe.app.entity.LoginSessions;
import recipe.app.entity.Users;
import recipe.app.repository.AuthCredentialsRepository;
import recipe.app.repository.LoginSessionsRepository;
import recipe.app.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository userRepository;
    private final AuthCredentialsRepository credentialRepository;
    private final LoginSessionsRepository sessionRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String login(String username, String password) {

        Users users = userRepository
                .findByUsernameAndDeleteFlg(username, 0)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        AuthCredentials cred = credentialRepository.findByUserId(users.getId())
                .orElseThrow(() -> new RuntimeException("CREDENTIAL_NOT_FOUND"));

        if (!encoder.matches(password, cred.getPasswordHash())) {
            throw new RuntimeException("INVALID_PASSWORD");
        }

        String token = UUID.randomUUID().toString();

        LoginSessions session = new LoginSessions();
        session.setUserId(users.getId());
        session.setSessionToken(token);
        session.setExpiresAt(LocalDateTime.now().plusDays(7));
        session.setRevoked(false);

        sessionRepository.save(session);

        return token;
    }
}
