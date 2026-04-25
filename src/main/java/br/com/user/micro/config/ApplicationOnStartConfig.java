package br.com.user.micro.config;

import br.com.user.micro.domain.User;
import br.com.user.micro.domain.Role;
import br.com.user.micro.exceptions.RoleNotFoundException;
import br.com.user.micro.repository.IUserRepository;
import br.com.user.micro.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ApplicationOnStartConfig {
    public HashMap<Integer, String> baseRoles = new HashMap<>(Map.of(
            0, "ADMIN",
            1, "BASIC"
    ));

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
            this.baseRoles.forEach((Integer id, String name) -> {
                roleRepository.findByName(name).orElseGet(() ->
                        roleRepository.save(Role.builder()
                                .id(id)
                                .name(name)
                                .build()
                        )
                );
            });
        };
    }

    @Bean
    @Order(2)
    public ApplicationRunner initAdmin(IUserRepository iUserRepository) {
        return args -> {
            boolean registeredUser = iUserRepository.existsByEmail(companyEmail);
            if (registeredUser) return;

            Role role = iRoleRepository.findById(0).orElseThrow(RoleNotFoundException::new);

            String firstName = "General";
            String lastName = "Admin";
            LocalDate birthday = LocalDate.now();
            String password = passwordEncoder.encode("@Esale2026");

            User user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .cpf(null)
                    .birthday(birthday)
                    .email(companyEmail)
                    .phone(null)
                    .address(null)
                    .role(role.getName())
                    .password(password)
                    .createdAt(LocalDateTime.now())
                    .build();

            iUserRepository.save(user);
        };
    }
}
