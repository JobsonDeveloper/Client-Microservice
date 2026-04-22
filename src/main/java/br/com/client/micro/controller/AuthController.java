package br.com.client.micro.controller;

import br.com.client.micro.domain.Client;
import br.com.client.micro.domain.Role;
import br.com.client.micro.domain.Session;
import br.com.client.micro.domain.complements.Address;
import br.com.client.micro.domain.complements.Phone;
import br.com.client.micro.dto.request.CreateClientDto;
import br.com.client.micro.dto.request.UserLoginDto;
import br.com.client.micro.dto.response.ClientAuthDto;
import br.com.client.micro.dto.response.ClientDto;
import br.com.client.micro.dto.response.ResponseMessageDto;
import br.com.client.micro.dto.swagger.DefaultErrorResponseDto;
import br.com.client.micro.dto.swagger.validation.fields.FieldsErrorDto;
import br.com.client.micro.exceptions.DifferentPasswordsException;
import br.com.client.micro.exceptions.PermissionDeniedException;
import br.com.client.micro.service.IClientService;
import br.com.client.micro.service.IRoleService;
import br.com.client.micro.service.ISessionService;
import br.com.client.micro.util.TokenGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@Tag(name = "Auth", description = "User authentication")
public class AuthController {
    private final IClientService iClientService;
    private final TokenGenerator tokenGenerator;
    private final ISessionService iSessionService;
    private final IRoleService iRoleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;

    public AuthController(
            IClientService iClientService,
            TokenGenerator tokenGenerator,
            ISessionService iSessionService,
            IRoleService iRoleService,
            BCryptPasswordEncoder passwordEncoder,
            JwtDecoder jwtDecoder
    ) {
        this.iClientService = iClientService;
        this.tokenGenerator = tokenGenerator;
        this.iSessionService = iSessionService;
        this.iRoleService = iRoleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping("/api/client/auth/register")
    @Transactional
    @Operation(
            summary = "Create a client",
            description = "Create a new client in the system",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Client registered successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientAuthDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Inconsistent request fields",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FieldsErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Internal resource not found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict in the process",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ClientAuthDto> register(@Valid @RequestBody CreateClientDto clientDto, HttpServletRequest request) {
        String firstName = clientDto.firstName();
        String lastName = clientDto.lastName();
        String cpf = clientDto.cpf();
        LocalDate birthday = clientDto.birthday();
        String email = clientDto.email();
        String password = clientDto.password();
        String confirmationPassword = clientDto.confirmationPassword();
        Phone phone = Phone.builder()
                .countryCode(clientDto.phone().countryCode())
                .cityCode(clientDto.phone().cityCode())
                .number(clientDto.phone().number())
                .build();
        Address address = Address.builder()
                .cep(clientDto.address().cep())
                .state(clientDto.address().state())
                .city(clientDto.address().city())
                .street(clientDto.address().street())
                .number(clientDto.address().number())
                .complement(clientDto.address().complement())
                .build();

        if (!password.equals(confirmationPassword)) throw new DifferentPasswordsException();

        Role role = iRoleService.findByName("BASIC");
        String encodedPassword = passwordEncoder.encode(password);
        Client client = Client.builder()
                .firstName(firstName)
                .lastName(lastName)
                .cpf(cpf)
                .birthday(birthday)
                .email(email)
                .phone(phone)
                .address(address)
                .role(role)
                .password(encodedPassword)
                .createdAt(LocalDateTime.now())
                .build();

        Client newClient = iClientService.createClient(client);
        Session session = iSessionService.startSession(newClient.getId(), request);
        String token = tokenGenerator.tokenConstructor(newClient.getId(), session.getId(), newClient.getRole().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ClientAuthDto(
                        "Client registered successfully!",
                        new ClientDto(newClient),
                        token
                )
        );
    }

    @PostMapping("/api/client/auth/login")
    @Operation(
            summary = "Client login",
            description = "Register the login of a client and return a access token",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login realized successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientAuthDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Inconsistent request fields",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FieldsErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid email or password!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Client not found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ClientAuthDto> login(@Valid @RequestBody UserLoginDto loginDto, HttpServletRequest request) {
        String email = loginDto.email();
        String password = loginDto.password();

        Client client = iClientService.getClientByEmail(email);
        boolean validPassword = passwordEncoder.matches(password, client.getPassword());

        if(!validPassword) throw new PermissionDeniedException("Invalid email or password!");

        String userId = client.getId();
        Session session = iSessionService.startSession(userId, request);
        String sessionId = session.getId();
        String roleId = client.getRole().getId();
        String token = tokenGenerator.tokenConstructor(userId, sessionId, roleId);

        return ResponseEntity.status(HttpStatus.OK).body(new ClientAuthDto(
                "Login realized successfully!",
                new ClientDto(client),
                token
        ));
    }

    @PostMapping("/api/client/auth/logout")
    @Operation(
            summary = "Client logout",
            description = "Finalize the session of client login",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Logout realized successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthenticated client",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Session not found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "503",
                            description = "Service Unavailable",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ResponseMessageDto> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer", "").trim();
        Jwt tokenInfo = jwtDecoder.decode(token);
        String sessionId = tokenInfo.getClaim("sessionId");

        iSessionService.endSession(sessionId);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDto("Logout realized successfully!"));
    }
}
