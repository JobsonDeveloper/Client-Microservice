package br.com.user.micro.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "sessions")
public class Session {
    @Id
    private String id;
    private String userId;
    private String ipAddress;
    private String userAgent;
    private String userDevice;
    private LocalDateTime loginOn;
    private LocalDateTime logoutOn;
}
