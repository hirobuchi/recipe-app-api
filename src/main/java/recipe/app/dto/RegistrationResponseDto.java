package recipe.app.dto;

import lombok.Data;

@Data
public class RegistrationResponseDto {
    private String credentialId; // Base64URL形式
    private String publicKey;    // Base64URL形式
    private Long userId;         // 紐付けるユーザID
}
