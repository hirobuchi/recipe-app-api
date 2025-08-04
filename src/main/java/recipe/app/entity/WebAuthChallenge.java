package recipe.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class WebAuthChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String challenge;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;
}