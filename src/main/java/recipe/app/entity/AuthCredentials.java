package recipe.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class AuthCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String passwordHash;

    private Integer failedCount;

    private LocalDateTime lockedUntil;

    private LocalDateTime lastLoginAt;
}
