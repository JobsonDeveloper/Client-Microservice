package br.com.user.micro.controller;

import br.com.user.micro.domain.User;
import br.com.user.micro.domain.Role;
import br.com.user.micro.domain.Session;
import br.com.user.micro.domain.complements.Address;
import br.com.user.micro.domain.complements.Phone;
import br.com.user.micro.dto.request.CreateUserDto;
import br.com.user.micro.dto.request.UserLoginDto;
import br.com.user.micro.dto.response.UserAuthDto;
import br.com.user.micro.dto.response.UserDto;
import br.com.user.micro.dto.response.ResponseMessageDto;
import br.com.user.micro.dto.swagger.DefaultErrorResponseDto;
import br.com.user.micro.dto.swagger.validation.fields.FieldsErrorDto;
import br.com.user.micro.exceptions.DifferentPasswordsException;
import br.com.user.micro.exceptions.PermissionDeniedException;
import br.com.user.micro.service.IUserService;
import br.com.user.micro.service.IRoleService;
import br.com.user.micro.service.ISessionService;
import br.com.user.micro.util.TokenGenerator;
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
    private final IUserService iUserService;
    private final TokenGenerator tokenGenerator;
    private final ISessionService iSessionService;
    private final IRoleService iRoleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;

    public AuthController(
            IUserService iUserService,
            TokenGenerator tokenGenerator,
            ISessionService iSessionService,
            IRoleService iRoleService,
            BCryptPasswordEncoder passwordEncoder,
            JwtDecoder jwtDecoder
    ) {
        this.iUserService = iUserService;
        this.tokenGenerator = tokenGenerator;
        this.iSessionService = iSessionService;
        this.iRoleService = iRoleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping("/api/user/auth/register")
    @Transactional
    @Operation(
            summary = "Create a user",
            description = "Create a new user in the system",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserAuthDto.class)
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
    public ResponseEntity<UserAuthDto> register(@Valid @RequestBody CreateUserDto UserDto, HttpServletRequest request) {
        String firstName = UserDto.firstName();
        String lastName = UserDto.lastName();
        String cpf = UserDto.cpf();
        LocalDate birthday = UserDto.birthday();
        String email = UserDto.email();
        String password = UserDto.password();
        String confirmationPassword = UserDto.confirmationPassword();
        Phone phone = Phone.builder()
                .countryCode(UserDto.phone().countryCode())
                .cityCode(UserDto.phone().cityCode())
                .number(UserDto.phone().number())
                .build();
        Address address = Address.builder()
                .cep(UserDto.address().cep())
                .state(UserDto.address().state())
                .city(UserDto.address().city())
                .street(UserDto.address().street())
                .number(UserDto.address().number())
                .complement(UserDto.address().complement())
                .build();

        if (!password.equals(confirmationPassword)) throw new DifferentPasswordsException();

        Role role = iRoleService.findByName("BASIC");
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
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

        User newUser = iUserService.create(user);
        Session session = iSessionService.startSession(newUser.getId(), request);
        String token = tokenGenerator.tokenConstructor(newUser.getId(), session.getId(), newUser.getRole().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new UserAuthDto(
                        "User registered successfully!",
                        new UserDto(newUser),
                        token
                )
        );
    }

    @PostMapping("/api/user/auth/login")
    @Operation(
            summary = "User login",
            description = "Register the login of a user and return a access token",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login realized successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserAuthDto.class)
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
                            description = "User not found!",
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
    public ResponseEntity<UserAuthDto> login(@Valid @RequestBody UserLoginDto loginDto, HttpServletRequest request) {
        String email = loginDto.email();
        String password = loginDto.password();

        User user = iUserService.getByEmail(email);
        boolean validPassword = passwordEncoder.matches(password, user.getPassword());

        if(!validPassword) throw new PermissionDeniedException("Invalid email or password!");

        String userId = user.getId();
        Session session = iSessionService.startSession(userId, request);
        String sessionId = session.getId();
        String roleId = user.getRole().getId();
        String token = tokenGenerator.tokenConstructor(userId, sessionId, roleId);

        return ResponseEntity.status(HttpStatus.OK).body(new UserAuthDto(
                "Login realized successfully!",
                new UserDto(user),
                token
        ));
    }

    @PostMapping("/api/user/auth/logout")
    @Operation(
            summary = "User logout",
            description = "Finalize the session of user login",
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
                            description = "Unauthenticated user",
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
