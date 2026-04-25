package br.com.user.micro.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Role {
    @Id
    private Integer id;
    private String name;
}
