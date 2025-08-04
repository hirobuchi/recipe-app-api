package recipe.app.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class WebAuthCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "credential_id", unique = true, nullable = false)
    private String credentialId;

    @Column(name = "public_key", nullable = false)
    private String publicKey;

    @Column(name = "sign_count")
    private Long signCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}