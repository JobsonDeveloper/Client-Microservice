package br.com.client.micro.config;

import br.com.client.micro.domain.Client;
import br.com.client.micro.domain.Role;
import br.com.client.micro.domain.complements.Address;
import br.com.client.micro.domain.complements.Phone;
import br.com.client.micro.exceptions.DifferentPasswordsException;
import br.com.client.micro.exceptions.ErrorExecutingOperationException;
import br.com.client.micro.exceptions.RoleNotFoundException;
import br.com.client.micro.repository.IClientRepository;
import br.com.client.micro.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class ApplicationOnStartConfig {
    public List<String> roles = List.of("ADMIN", "BASIC");

    @Value("${company.email}")
    public String companyEmail;

    private final IRoleRepository iRoleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public ApplicationOnStartConfig(
            IRoleRepository iRoleRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.iRoleRepository = iRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Order(1)
    public ApplicationRunner initRoles(IRoleRepository roleRepository) {
        return args -> {
            this.roles.forEach((String name) -> {
                roleRepository.findByName(name).orElseGet(() ->
                        roleRepository.save(Role.builder()
                                .name(name)
                                .build()
                        )
                );
            });
        };
    }

    @Bean
    @Order(2)
    public ApplicationRunner initAdmin(IClientRepository iClientRepository) {
        return args -> {
            boolean registeredClient = iClientRepository.existsByEmail(companyEmail);
            if (registeredClient) return;

            Role role = iRoleRepository.findByName("ADMIN").orElseThrow(RoleNotFoundException::new);

            String firstName = "General";
            String lastName = "Admin";
            LocalDate birthday = LocalDate.now();
            String password = passwordEncoder.encode("@Esale2026");

            Client client = Client.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .cpf(null)
                    .birthday(birthday)
                    .email(companyEmail)
                    .phone(null)
                    .address(null)
                    .role(role)
                    .password(password)
                    .createdAt(LocalDateTime.now())
                    .build();

            iClientRepository.save(client);
        };
    }
}
