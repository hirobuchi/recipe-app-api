package recipe.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @Column(name = "delete_flg")
    private Integer deleteFlg;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
