package br.com.client.micro.config;

import br.com.client.micro.domain.Role;
import br.com.client.micro.repository.IRoleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RolesConfig {
    @Bean
    public ApplicationRunner initRoles(IRoleRepository roleRepository) {
        return args -> {
            List<String> roles = List.of("ADMIN", "USER");

            roles.forEach((String name) -> {
                roleRepository.findByName(name).orElseGet(() ->
                        roleRepository.save(Role.builder()
                                .name(name)
                                .build()
                        )
                );
            });
        };
    }
}
