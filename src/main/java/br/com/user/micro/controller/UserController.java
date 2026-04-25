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
import br.com.user.micro.service.imp.UserService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Tag(name = "User", description = "User operations")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/api/user/{id}/delete")
    @Transactional
    @PreAuthorize("hasRole('ADMIN', 'BASIC')")
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
            @PathVariable String id
    ) {
        userService.delete(id);
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
    public ResponseEntity<UserInfoDto> updateUserData(@Valid @RequestBody ChangeUserDataDto userDto) {
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

        User user = userService.update(currentUser);

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
            @PathVariable String id
    ) {
        User user = userService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new UserInfoDto(
                        "User information returned successfully!",
                        new UserDto(user)
                )
        );
    }

    @GetMapping("/api/user/list")
    @PreAuthorize("hasRole('ADMIN')")
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
            @RequestParam(defaultValue = "10", required = false, name = "size") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<UserDto> userws = userService.list(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(PageUserResponseDto.from(userws));
    }
}
