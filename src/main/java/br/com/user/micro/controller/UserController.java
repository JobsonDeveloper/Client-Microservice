package br.com.user.micro.controller;

import br.com.user.micro.domain.complements.Address;
import br.com.user.micro.domain.complements.Phone;
import br.com.user.micro.dto.response.UserDto;
import br.com.user.micro.dto.response.UserInfoDto;
import br.com.user.micro.dto.response.ResponseMessageDto;
import br.com.user.micro.dto.swagger.DefaultErrorResponseDto;
import br.com.user.micro.dto.swagger.PageUserResponseDto;
import br.com.user.micro.domain.User;
import br.com.user.micro.dto.swagger.validation.fields.FieldsErrorDto;
import br.com.user.micro.dto.request.ChangeUserDataDto;
import br.com.user.micro.exceptions.PermissionDeniedException;
import br.com.user.micro.service.IUserService;
import br.com.user.micro.util.TokenCredentialsExtractor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Tag(name = "User", description = "User operations")
public class UserController {

    private final IUserService iUserService;

    private final TokenCredentialsExtractor tokenExtractor;

    public UserController(
            IUserService iUserService,
            TokenCredentialsExtractor tokenExtractor
    ) {
        this.iUserService = iUserService;
        this.tokenExtractor = tokenExtractor;
    }

    @DeleteMapping("/api/user/{id}/delete")
    @Transactional
    @Operation(
            summary = "Delete a user",
            description = "Delete a user of the system",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseMessageDto.class)
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
                            description = "Unauthenticated user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Incompatible user role",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
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
    public ResponseEntity<ResponseMessageDto> deleteUser(
            @Parameter(description = "User id", required = true)
            @PathVariable String id,
            @RequestHeader("Authorization") String authorization
    ) {
        Boolean valid = tokenExtractor.userValidator(id, authorization);
        if (!valid) throw new AuthorizationDeniedException("");

        iUserService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseMessageDto("User deleted successfully!")
        );
    }

    @PatchMapping("/api/user/update")
    @Transactional
    @Operation(
            summary = "Update user information",
            description = "Update the information of a user",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User information updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserInfoDto.class)
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
                            description = "Unauthenticated user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Incompatible user role",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
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
    public ResponseEntity<UserInfoDto> updateUserData(
            @Valid @RequestBody ChangeUserDataDto userDto,
            @RequestHeader("Authorization") String authorization
    ) {
        String id = userDto.id();
        String firstName = userDto.firstName();
        String lastName = userDto.lastName();
        LocalDate birthday = userDto.birthday();
        Phone phone = Phone.builder()
                .countryCode(userDto.phone().countryCode())
                .cityCode(userDto.phone().cityCode())
                .number(userDto.phone().number())
                .build();
        Address address = Address.builder()
                .cep(userDto.address().cep())
                .state(userDto.address().state())
                .city(userDto.address().city())
                .street(userDto.address().street())
                .number(userDto.address().number())
                .complement(userDto.address().complement())
                .build();
        User currentUser = User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .phone(phone)
                .address(address)
                .build();

        HashMap<String, String> credentials = tokenExtractor.extractor(authorization);

        if (credentials.get("role").equals("BASIC")) {
            if (!credentials.get("id").equals(id)) throw new AuthorizationDeniedException("");
        }

        User user = iUserService.update(currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new UserInfoDto(
                        "User information updated successfully!",
                        new UserDto(user)
                )
        );
    }

    @GetMapping("/api/user/{id}/info")
    @Operation(
            summary = "Get user information",
            description = "Return all information of a user",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User information returned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserInfoDto.class)
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
                            responseCode = "403",
                            description = "Incompatible user role",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DefaultErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
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
    public ResponseEntity<UserInfoDto> getUserInfo(
            @Parameter(description = "User id", required = true)
            @PathVariable String id,
            @RequestHeader("Authorization") String authorization
    ) {
        HashMap<String, String> credentials = tokenExtractor.extractor(authorization);

        if (credentials.get("role").equals("BASIC")) {
            if (!credentials.get("id").equals(id)) throw new AuthorizationDeniedException("");
        }

        User user = iUserService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new UserInfoDto(
                        "User information returned successfully!",
                        new UserDto(user)
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/user/list")
    @Operation(
            summary = "List users",
            description = "Return a list of users",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User list returned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = PageUserResponseDto.class
                                    )
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
                            responseCode = "403",
                            description = "Incompatible user role",
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
    public ResponseEntity<PageUserResponseDto> listUsers(
            @RequestParam(defaultValue = "0", required = false, name = "page") int page,
            @RequestParam(defaultValue = "10", required = false, name = "size") int size,
            @RequestHeader("Authorization") String authorization
    ) {
        HashMap<String, String> credentials = tokenExtractor.extractor(authorization);
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<UserDto> userList = iUserService.list(pageRequest, credentials.get("role"));

        return ResponseEntity.status(HttpStatus.OK).body(PageUserResponseDto.from(userList));
    }
}
