package tn.esprit.cloud_in_mypocket.dto.IaDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAIDTO {
    private Long userId;
    private String nom;
    private String prenom;
    private String email;
    private String numeroDeTelephone;
    private String role;
    private Boolean active;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
} 