package br.com.client.micro.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
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
